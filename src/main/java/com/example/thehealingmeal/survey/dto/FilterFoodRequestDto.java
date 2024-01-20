package com.example.thehealingmeal.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterFoodRequestDto {
    private String stewsAndHotpots; // 찌개와 전골류

    private String stewedFood; // 조림류

    private String stirFriedFood; // 볶음류

    private String grilledFood; // 구이류

    private String vegetableFood; // 나물 숙채류

    private String steamedFood; // 찜류

    private String pancakeFood; // 전 적 및 부침류

    private String breadAndConfectionery; // 빵 및 과자류

    private String beveragesAndTeas; // 음료 및 차류

    private String dairyProducts; // 유제품류
}
