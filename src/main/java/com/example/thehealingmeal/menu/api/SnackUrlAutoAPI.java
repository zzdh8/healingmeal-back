package com.example.thehealingmeal.menu.api;

import com.example.thehealingmeal.menu.service.SnackUrlService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SnackUrlAutoAPI {
    private final SnackUrlService service;

    @PostConstruct
    @GetMapping("save")
    public String SaveUrl(){
        service.urlSave();
        return "Url is saved.";
    }
}
