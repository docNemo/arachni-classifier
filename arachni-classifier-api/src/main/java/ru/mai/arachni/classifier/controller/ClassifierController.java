package ru.mai.arachni.classifier.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.arachni.classifier.dto.response.CategoryClassifierResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/classifier")
public class ClassifierController {

    @PostMapping("/category")
    public CategoryClassifierResponse getClassifiedCategory() {
        return new CategoryClassifierResponse("Это просто нечто");
    }
}
