package ru.mai.arachni.classifier.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mai.arachni.classifier.TextClassifier;
import ru.mai.arachni.classifier.dto.request.CategoryClassifierRequest;
import ru.mai.arachni.classifier.dto.response.CategoryClassifierResponse;
import ru.mai.arachni.classifier.provider.ModelProvider;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.PART;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.NullStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.Tokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ClassifierService {
    private final ModelProvider modelProvider;
    private final String[] categories = new String[]{
            "EliteDangerous",
            "HarryPotter",
            "Metro",
            "StarWars",
            "TheElderScrolls"
    };


    @SneakyThrows
    public CategoryClassifierResponse getCategory(
            CategoryClassifierRequest categoryClassifierRequest
    ) {

        Attribute attribute = new Attribute("text", (ArrayList<String>) null);

        Instances instances = new Instances(
                "article",
                new ArrayList<>(List.of(
                        attribute
                )),
                0
        );

        double[] instanceValue1 = new double[instances.numAttributes()];

        instanceValue1[0] = instances
                .attribute(0)
                .addStringValue(categoryClassifierRequest.getText());

        instances.add(new DenseInstance(1.0, instanceValue1));

        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(instances);
        NGramTokenizer tokenizer = new NGramTokenizer();
        filter.setTokenizer(tokenizer);
        tokenizer.setNGramMaxSize(3);
        tokenizer.setNGramMinSize(1);
        tokenizer.setDelimiters(" \r\n\t.,;:'\"()?!");

        InputStream datasetStream = modelProvider.getDataSetType();
        Instances dataset = new Instances(new InputStreamReader(datasetStream));

        Instances cook = Filter.useFilter(
                instances,
                filter
        );

        LOGGER.info("cook: {}", cook);

        InputStream modelInputStream = modelProvider.getModel();
        PART classifier = (PART) weka.core.SerializationHelper.read(modelInputStream);

        LOGGER.info("cap: {}", classifier.getCapabilities().capabilities());
//        dataset.add(cook.get(0));
        LOGGER.info("num attr cook: {}, dataset: {}", cook.numAttributes(), dataset.numAttributes());
        dataset.setClassIndex(0);
        LOGGER.info("new ds {}", dataset);
        LOGGER.info("dataset class ind: {}", dataset.classAttribute());
//        cook.setClassIndex(cook.numAttributes() - 1);
        cook.get(0).setDataset(dataset);
        LOGGER.info("new cook {}", cook);
        int instanceClass = (int) classifier.classifyInstance(cook.get(0));
        LOGGER.info("res: {} - {}", instanceClass, categories[instanceClass]);
        return new CategoryClassifierResponse("Это нечто: " + instanceClass);
    }
}