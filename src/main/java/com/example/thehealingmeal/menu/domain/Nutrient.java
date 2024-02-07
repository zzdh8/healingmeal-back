package com.example.thehealingmeal.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Nutrient {
    private int kcal;
    private float protein;
    private float carbohydrate;
    private float fat;

    public Nutrient add(Nutrient other) {
        this.kcal += other.kcal;
        this.protein += other.protein;
        this.carbohydrate += other.carbohydrate;
        this.fat += other.fat;
        return this;
    }
}
