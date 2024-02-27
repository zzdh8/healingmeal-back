package com.example.thehealingmeal.data.controller;

import com.example.thehealingmeal.data.service.FoodService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class foodController {

    private final FoodService foodService;

    @PostConstruct // 실행시에 자동으로 db 저장.
    @GetMapping("load_save")
    public String loadSave() throws Exception {
        foodService.loadSave();
        foodService.saveRiceCategoryFoods();
        foodService.saveSnackOrTeaCategoryFoods();
        foodService.saveSideDishCategoryFoods();
        foodService.saveMainDishCategoryFoods();
        return "download_success";
    }
}