package com.example.thehealingmeal.survey.domain;


import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Nutrient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    @Id
    @Column(name = "surveyResult_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int Kcal;
    private float protein;
    private float carbohydrate;
    private float fat;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 정적 팩토리 메서드
    public static SurveyResult createSurveyResult(int kcal, float protein, float fat, float carbohydrate, User user) {
        return SurveyResult.builder()
                .Kcal(kcal)
                .protein(protein)
                .fat(fat)
                .carbohydrate(carbohydrate)
                .user(user)
                .build();
    }

    public void update(SurveyResult surveyResult){
        this.Kcal = surveyResult.getKcal();
        this.protein = surveyResult.getProtein();
        this.carbohydrate = surveyResult.getCarbohydrate();
        this.fat = surveyResult.getFat();
    }

    public boolean exceeds(Nutrient totalNutrients) {
        return totalNutrients.getKcal() > getKcal() ||
                totalNutrients.getProtein() > getProtein() ||
                totalNutrients.getCarbohydrate() > getCarbohydrate() ||
                totalNutrients.getFat() > getFat();
    }
}
