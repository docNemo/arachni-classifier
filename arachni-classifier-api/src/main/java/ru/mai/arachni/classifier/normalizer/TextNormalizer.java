package ru.mai.arachni.classifier.normalizer;

import company.evo.jmorphy2.MorphAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class TextNormalizer {
    private static final Pattern PATTERN = Pattern.compile(
            "^\\W*?([а-яёА-ЯЁ]+)\\W*?$"
    );

    private final MorphAnalyzer morphy;

    @SneakyThrows
    List<String> getNormalForm(String word) {
        return morphy.normalForms(word);
    }

    public String normalizeText(String text) {
        return lemmatizeWords(deletePunctuation(text));
    }

    String deletePunctuation(String text) {
        return text.replaceAll("[.,;:'\"()?!]", "");
    }

    @SneakyThrows
    String lemmatizeWords(String text) {
        String[] words = text.split("\\s");
        for (int i = 0; i < words.length; i++) {
            Matcher match = PATTERN.matcher(words[i]);
            if (match.find()) {
                words[i] = words[i].replaceAll(
                        match.group(1),
                        getNormalForm(match.group(1)).get(0)
                );
            }
        }
        return String.join(" ", words);
    }
}
