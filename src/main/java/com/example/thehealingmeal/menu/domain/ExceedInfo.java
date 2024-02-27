package com.example.thehealingmeal.menu.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExceedInfo {
    //초과된 식단 중에서 가장 칼로리가 높은 식단
    Meals meals;
    //얼마나 초과됐는지를 보여주는 수치
    int kcalExceed;
    float carbohydrateExceed;
    float proteinExceed;
    float fatExceed;
}
