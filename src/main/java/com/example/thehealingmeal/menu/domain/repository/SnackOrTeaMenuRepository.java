package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.SnackOrTea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackOrTeaMenuRepository extends JpaRepository<SnackOrTea, Long> {
    SnackOrTea findByUserIdAndMeals(long userId, Meals meals);
}
