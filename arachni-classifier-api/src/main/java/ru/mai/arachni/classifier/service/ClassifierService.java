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

        LOGGER.info(
                "result class ind {}, category {}",
                instanceClass,
                instances
                        .firstInstance()
                        .value(0)
        );

        return new CategoryClassifierResponse(
                instances
                        .firstInstance()
                        .stringValue(0)
        );
    }

    Instances buildInstances(
            CategoryClassifierRequest categoryClassifierRequest
    ) {

        Instances dataset = getDataset();

        LOGGER.info("dataset: {}", dataset);
        double[] instanceValue = new double[dataset.numAttributes()];

        instanceValue[1] = dataset
                .attribute(1)
                .addStringValue(categoryClassifierRequest.getText());

        LOGGER.info("value: {}", instanceValue);

        dataset.add(new DenseInstance(1, instanceValue));
        LOGGER.info("instances: {}", dataset);
        dataset.setClassIndex(0);
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
