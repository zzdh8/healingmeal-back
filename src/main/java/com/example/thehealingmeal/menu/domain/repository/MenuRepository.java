package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.MenuForUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuForUser, Long> {
    MenuForUser findByUserIdAndMeals(long userId, Meals meals);
}
