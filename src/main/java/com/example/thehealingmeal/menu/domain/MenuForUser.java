package com.example.thehealingmeal.menu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.thehealingmeal.member.domain.User;
import jakarta.persistence.*;
import lombok.*;


//식품 테이블의 음식들을 조합 및 포함하여 DB에 저장할 domain
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuForUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    //대표메뉴 = 구이류, 찌개 및 전골류, 전적 및 부침류, 찜류
    private String main_dish;


    //밥 = 밥류
    private String rice;

    //아침, 간식, 점심, 저녁
    @Enumerated(EnumType.STRING)
    private Meals meals;

    //이 식단의 종합 열탄단지
    private int kcal;
    private float protein;
    private float carbohydrate;
    private float fat;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /*public static MenuForUser create(MenuForUser menuForUser){
        return MenuForUser.builder()
                .build();
    }*/
}

