package com.example.thehealingmeal.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SurveyResultDto {
    private int Kcal;
    private float protein;
    private float carbohydrate;
    private float fat;


    public SurveyResultDto(int Kcal, float protein, float carbohydrate, float fat) {
        this.Kcal = Kcal;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
    }
}
