package com.example.thehealingmeal.menu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.thehealingmeal.member.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnackOrTea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snack_or_tea_id")
    private Long id;

    //간식 이름
    private String snack_or_tea;

    //검색을 위한 간식 이름과 확장자
    private String imageUrl;

    //간식 타임 : 아점 사이, 점저 사이
    private Meals meals;

    //간식의 종합 열탄단지
    private int kcal;
    private float protein;
    private float carbohydrate;
    private float fat;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
