package ru.mai.arachni.classifier.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.mai.arachni.classifier.provider.ModelProvider;
import ru.mai.arachni.classifier.service.ClassifierService;

@Configuration
public class ClassifierConfiguration {
    @Bean
    public ModelProvider modelProvider(
            RestTemplate restTemplate,
            @Value("${model-storage.getModelUrl}") String getModelUrl,
            @Value("${model-storage.modelName}") String modelName
    ) {
        return new ModelProvider(
                restTemplate,
                getModelUrl,
                modelName
        );
    }

    @Bean
    public ClassifierService classifierService(
            ModelProvider modelProvider
    ) {
        return new ClassifierService(
                modelProvider
        );
    }
}
