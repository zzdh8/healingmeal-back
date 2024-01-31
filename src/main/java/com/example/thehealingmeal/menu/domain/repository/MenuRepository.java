package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.MenuForUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuForUser, Long> {
    MenuForUser findByUserIdAndMeals(long userId, Meals meals);

    List<MenuForUser> deleteAllByUserId(long userId);

    List<MenuForUser> findAllByUserId(long userId);

    MenuForUser findByUserAndMeals(User user, Meals meals);
}
