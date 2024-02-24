package com.example.thehealingmeal.menu.domain;

import com.example.thehealingmeal.member.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;


    private String main_dish;

    //대표메뉴 이미지 name
    private String imageURL;

    //밥 = 밥류
    private String rice;

    //아침, 점심, 저녁(간식은 따로)
    private Meals meals;

    //반찬류

    @ElementCollection
    @CollectionTable(name = "side_dish", joinColumns = @JoinColumn(name = "bookmark_id"))
    @Column(name = "side_dish")
    private List<String> sideDishForUserMenu;

    //이 식단의 종합 열탄단지
    private int kcal;
    private float protein;
    private float carbohydrate;
    private float fat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
