package com.example.thehealingmeal.menu.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SideDishForUserMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "side_dish_sequence_id")
    private Long id;

    //반찬 1~3개 = 김치류, 볶음류, 나물 숙채류, 생채 무침류, 수조어육류, 장아찌 절임류, 젓갈류, 조림류
    private String side_dish;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuForUser menuForUser;

}
