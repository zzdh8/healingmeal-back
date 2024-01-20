package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Meals;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponseDto {
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
}
