package ru.mai.arachni.classifier.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mai.arachni.classifier.dto.request.CategoryClassifierRequest;
import ru.mai.arachni.classifier.dto.response.CategoryClassifierResponse;
import ru.mai.arachni.classifier.provider.ModelProvider;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@RequiredArgsConstructor
public class ClassifierService {
    private static final int CATEGORY_ATTRIBUTE_INDEX = 0;
    private static final int TEXT_ATTRIBUTE_INDEX = 1;

    private final ModelProvider modelProvider;

    private Classifier classifier;

    @PostConstruct
    void initClassifier() {
        classifier = getClassifier();
    }

    @SneakyThrows
    public CategoryClassifierResponse getCategory(
            CategoryClassifierRequest categoryClassifierRequest
    ) {
        Instances instances = buildInstances(categoryClassifierRequest);

        int instanceClass = (int) classifier.classifyInstance(instances.firstInstance());
        String category = instances
                .attribute(CATEGORY_ATTRIBUTE_INDEX)
                .value(instanceClass);

        LOGGER.info(
                "result class ind {}, category {}",
                instanceClass,
                category
        );

        return new CategoryClassifierResponse(
                category
        );
    }

    Instances buildInstances(
            CategoryClassifierRequest categoryClassifierRequest
    ) {

        Instances dataset = getDataset();

        LOGGER.info("dataset: {}", dataset);
        double[] instanceValue = new double[dataset.numAttributes()];

        instanceValue[TEXT_ATTRIBUTE_INDEX] = dataset
                .attribute(TEXT_ATTRIBUTE_INDEX)
                .addStringValue(categoryClassifierRequest.getText());

        LOGGER.info("value: {}", instanceValue);

        dataset.add(new DenseInstance(1, instanceValue));
        LOGGER.info("instances: {}", dataset);
        dataset.setClassIndex(CATEGORY_ATTRIBUTE_INDEX);
        dataset.firstInstance().setClassMissing();

        return dataset;
    }

    @SneakyThrows
    Classifier getClassifier() {
        InputStream modelInputStream = modelProvider.getModel();
        return (Classifier) SerializationHelper.read(modelInputStream);
    }

    @SneakyThrows
    Instances getDataset() {
        InputStream datasetStream = modelProvider.getDataSetType();
        return new Instances(new InputStreamReader(datasetStream));
    }
}
