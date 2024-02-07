package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.Nutrient;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponseDto {
    private Long bookmarkId;
    //대표메뉴 = 구이류, 찌개 및 전골류, 전적 및 부침류, 찜류
    private String main_dish;

    //대표메뉴 이미지 name
    private String imageURL;

    //밥 = 밥류
    private String rice;

    //아침, 점심, 저녁(간식은 따로)
    private Meals meals;

    //반찬류
    private List<String> sideDishForUserMenu;

    //이 식단의 종합 열탄단지
    private int kcal;
    private float protein;
    private float carbohydrate;
    private float fat;

    private long user_id;

    @JsonIgnore
    //유저 구분
    private User user;

    @Builder
    public static MenuResponseDto createMenu(Long bookmarkId, String main_dish, String imageURL, String rice,
                                             Meals meals, List<String> sideDishForUserMenu,
                                             int kcal, float protein, float carbohydrate, float fat) {
        MenuResponseDto menuResponseDto = new MenuResponseDto();
        menuResponseDto.bookmarkId = bookmarkId;
        menuResponseDto.main_dish = main_dish;
        menuResponseDto.imageURL = imageURL;
        menuResponseDto.rice = rice;
        menuResponseDto.meals = meals;
        menuResponseDto.sideDishForUserMenu = sideDishForUserMenu;
        menuResponseDto.kcal = kcal;
        menuResponseDto.protein = protein;
        menuResponseDto.carbohydrate = carbohydrate;
        menuResponseDto.fat = fat;
        return menuResponseDto;
    }

    //total nutrients
    @JsonIgnore
    public Nutrient getTotalNutrients() {
        return new Nutrient(this.kcal, this.protein, this.carbohydrate, this.fat);
    }

}
