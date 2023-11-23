package ru.mai.arachni.classifier.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/classifier")
public class ClassifierController {

    @PostMapping("/category")
    public String test() {
        return "Это просто нечто";
    }
}
