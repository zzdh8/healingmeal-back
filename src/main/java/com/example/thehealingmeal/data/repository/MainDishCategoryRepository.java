package com.example.thehealingmeal.data.repository;

import com.example.thehealingmeal.data.domain.MainDishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainDishCategoryRepository extends JpaRepository<MainDishCategory, Long> {
    List<MainDishCategory> findMainDishCategoriesByKcalLessThanOrCarbohydrateLessThanOrProteinLessThanOrFatLessThanOrderByKcalAsc(int kcal, float carbohydrate, float protein, float fat);
    List<MainDishCategory> findMainDishCategoriesByProteinLessThanOrderByProteinAsc(float protein);
    Optional<MainDishCategory> findMainDishCategoryByRepresentativeFoodName(String representativeFoodName);
}
