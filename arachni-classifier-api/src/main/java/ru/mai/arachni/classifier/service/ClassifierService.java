package ru.mai.arachni.classifier.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mai.arachni.classifier.dto.request.CategoryClassifierRequest;
import ru.mai.arachni.classifier.dto.response.CategoryClassifierResponse;
import ru.mai.arachni.classifier.provider.ModelProvider;

import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class ClassifierService {
    private final ModelProvider modelProvider;
    @SneakyThrows
    public CategoryClassifierResponse getCategory(
            CategoryClassifierRequest categoryClassifierRequest
    ) {
        InputStream modelInputStream = modelProvider.getModel();

        LOGGER.info(modelInputStream.toString());
        LOGGER.info(new String(modelInputStream.readAllBytes()));

        // classification

        return new CategoryClassifierResponse("Это нечто");
    }
}
