package com.example.thehealingmeal.data.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SnackOrTeaCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;
    private String foodCategory;
    private String representativeFoodName;
    private int Kcal;
    private float protein;
    private float fat;
    private float carbohydrate;
    private float sugar;
    private int sodium;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @Builder
    public SnackOrTeaCategory(String foodName, String foodCategory, String representativeFoodName, int Kcal, float protein, float fat, float carbohydrate, float sugar, int sodium) {
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.representativeFoodName = representativeFoodName;
        this.Kcal = Kcal;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.sugar = sugar;
        this.sodium = sodium;
    }
}
