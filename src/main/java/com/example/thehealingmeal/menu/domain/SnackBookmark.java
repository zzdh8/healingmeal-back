package com.example.thehealingmeal.menu.domain;

import com.example.thehealingmeal.member.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnackBookmark implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snack_bookmark_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
