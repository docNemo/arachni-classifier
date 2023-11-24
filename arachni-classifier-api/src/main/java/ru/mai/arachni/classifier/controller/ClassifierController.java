package ru.mai.arachni.classifier.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.arachni.classifier.dto.request.CategoryClassifierRequest;
import ru.mai.arachni.classifier.dto.response.CategoryClassifierResponse;
import ru.mai.arachni.classifier.service.ClassifierService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/classifier")
public class ClassifierController {
    private final ClassifierService classifierService;

    @PostMapping("/category")
    public CategoryClassifierResponse getClassifiedCategory(
            @RequestBody CategoryClassifierRequest categoryClassifierRequest
    ) {
        return classifierService.getCategory(categoryClassifierRequest);
    }
}
