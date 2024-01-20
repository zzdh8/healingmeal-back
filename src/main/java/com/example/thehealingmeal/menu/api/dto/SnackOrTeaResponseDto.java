package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Meals;
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
}
