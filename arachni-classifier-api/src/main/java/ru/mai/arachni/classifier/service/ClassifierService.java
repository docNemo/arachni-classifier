package ru.mai.arachni.classifier.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mai.arachni.classifier.dto.request.CategoryClassifierRequest;
import ru.mai.arachni.classifier.dto.response.CategoryClassifierResponse;
import ru.mai.arachni.classifier.provider.ModelProvider;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.Filter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ClassifierService {
    private static final String[] CATEGORIES = new String[]{
            "Elite Dangerous",
            "Гарри Поттер",
            "Метро",
            "Звёздные войны",
            "The Elder Scrolls",
            "Другое"
    };

    private final ModelProvider modelProvider;
    private final Filter filter;

    private Classifier classifier;
    private Instances dataset;

    @PostConstruct
    void initClassifier() {
        classifier = getClassifier();
        dataset = getDataset();

        dataset.setClassIndex(0);
    }

    @SneakyThrows
    public CategoryClassifierResponse getCategory(
            CategoryClassifierRequest categoryClassifierRequest
    ) {
        Instances instances = buildInstances(categoryClassifierRequest);
//        filter.setInputFormat(instances);
//
//        Instances vectorizedText = Filter.useFilter(
//                instances,
//                filter
//        );
//
//        vectorizedText.get(0).setDataset(dataset);
//        LOGGER.info("new vectorizedText {}", vectorizedText);

//        LOGGER.info("inst: {}", instances);
        instances.instance(0).setClassMissing();
        int instanceClass = (int) classifier.classifyInstance(instances.instance(0));
//        int instanceClass = (int) classifier.classifyInstance(vectorizedText.get(0));
        LOGGER.info("result class ind {}, название {}", instanceClass, CATEGORIES[instanceClass]);
        return new CategoryClassifierResponse(CATEGORIES[instanceClass]);
    }

    Instances buildInstances(
            CategoryClassifierRequest categoryClassifierRequest
    ) {
        Attribute attribute = new Attribute("text", (FastVector) null);
        Attribute universe = new Attribute("universe");

        Instances instances = new Instances(
                "article",
                new ArrayList<>(List.of(
                        attribute,
                        universe
                )),
                0
        );
        LOGGER.info("i: {}", instances);
        double[] instanceValue1 = new double[instances.numAttributes()];

        instanceValue1[0] = instances
                .attribute(attribute.name())
                .addStringValue(categoryClassifierRequest.getText());

        LOGGER.info("val: {}", instanceValue1);

        instances.add(new DenseInstance(1, instanceValue1));
        LOGGER.info("in: {}", instances);
        instances.setClass(universe);

        return instances;
    }

    @SneakyThrows
    Classifier getClassifier() {
        InputStream modelInputStream = modelProvider.getModel();
        return (Classifier) weka.core.SerializationHelper.read(modelInputStream);
    }

    @SneakyThrows
    Instances getDataset() {
        InputStream datasetStream = modelProvider.getDataSetType();
        return new Instances(new InputStreamReader(datasetStream));
    }
}
