package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.Nutrient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SnackOrTeaResponseDto {
    private Long bookmarkId;
    //간식 이름
    private String snack_or_tea;

    //간식 이미지 URL
    private String imageURL;

    private Meals meals;

    //간식의 종합 열탄단지
    private int kcal;
    private float protein;
    private float carbohydrate;
    private float fat;

    private long userId;

    @JsonIgnore
    //유저 구분
    private User user;
    @Builder
    public static SnackOrTeaResponseDto createMenu(Long bookmarkId,String snack_or_tea, String imageURL,
                                             Meals meals,
                                             int kcal, float protein, float carbohydrate, float fat) {
        SnackOrTeaResponseDto snackOrTeaResponseDto = new SnackOrTeaResponseDto();
        snackOrTeaResponseDto.bookmarkId = bookmarkId;
        snackOrTeaResponseDto.snack_or_tea = snack_or_tea;
        snackOrTeaResponseDto.imageURL = imageURL;
        snackOrTeaResponseDto.meals = meals;
        snackOrTeaResponseDto.kcal = kcal;
        snackOrTeaResponseDto.protein = protein;
        snackOrTeaResponseDto.carbohydrate = carbohydrate;
        snackOrTeaResponseDto.fat = fat;
        return snackOrTeaResponseDto;
    }

    //total Nutrients
    @JsonIgnore
    public Nutrient getTotalNutrients() {
        return new Nutrient(this.kcal, this.protein, this.carbohydrate, this.fat);
    }
}
