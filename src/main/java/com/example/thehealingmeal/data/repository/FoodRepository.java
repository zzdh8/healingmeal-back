package com.example.thehealingmeal.data.repository;

import com.example.thehealingmeal.data.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByFoodCategory(String foodCategory);
}
