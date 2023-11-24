package ru.mai.arachni.classifier.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.mai.arachni.classifier.provider.ModelProvider;
import ru.mai.arachni.classifier.service.ClassifierService;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

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
            Filter filter
    ) {
        return new ClassifierService(
                modelProvider,
                filter
        );
    }

    @Bean
    public Filter filterStringToWordVec() {
        StringToWordVector filter = new StringToWordVector();
        NGramTokenizer tokenizer = new NGramTokenizer();
        filter.setTokenizer(tokenizer);
        tokenizer.setNGramMaxSize(3);
        tokenizer.setNGramMinSize(1);
        tokenizer.setDelimiters(" \r\n\t.,;:'\"()?!");
        return filter;
    }
}
