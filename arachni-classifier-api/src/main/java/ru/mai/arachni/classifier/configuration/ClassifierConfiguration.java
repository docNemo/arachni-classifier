package ru.mai.arachni.classifier.configuration;

import company.evo.jmorphy2.MorphAnalyzer;
import company.evo.jmorphy2.ResourceFileLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.mai.arachni.classifier.normalizer.TextNormalizer;
import ru.mai.arachni.classifier.provider.ModelProvider;
import ru.mai.arachni.classifier.service.ClassifierService;

import java.io.IOException;

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
            ModelProvider modelProvider,
            TextNormalizer textNormalizer
    ) {
        return new ClassifierService(
                modelProvider,
                textNormalizer
        );
    }

    @Bean
    public TextNormalizer textNormalizer(
            MorphAnalyzer morphAnalyzer
    ) {
        return new TextNormalizer(
                morphAnalyzer
        );
    }

    @Bean
    public MorphAnalyzer morphAnalyzer(
            @Value("${morphy.path}") String path
    )
            throws IOException {
        return new MorphAnalyzer.Builder()
                .fileLoader(new ResourceFileLoader(path))
                .charSubstitutes(null)
                .build();
    }
}
