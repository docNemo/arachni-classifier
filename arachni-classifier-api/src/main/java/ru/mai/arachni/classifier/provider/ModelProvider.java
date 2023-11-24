package ru.mai.arachni.classifier.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@RequiredArgsConstructor
public class ModelProvider {
    private final RestTemplate restTemplate;
    private final String getModelUrl;
    private final String modelFileName;

    public InputStream getModel() {
        return new ByteArrayInputStream(
                Base64.getDecoder().decode(
                        restTemplate.getForObject(getModelUrl + modelFileName, String.class)
                )
        );
    }
}
