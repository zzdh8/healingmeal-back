package com.example.thehealingmeal.data.repository;

import com.example.thehealingmeal.data.domain.SideDishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SideDishCategoryRepository extends JpaRepository<SideDishCategory, Long> {
   List<SideDishCategory> findSideDishCategoryByKcalLessThanOrCarbohydrateLessThanOrProteinLessThanOrFatLessThanOrderByKcalAsc(int kcal, float carbohydrate, float protein, float fat);
   List<SideDishCategory> findSideDishCategoryByProteinLessThanOrderByProteinAsc(float protein);
   Optional<SideDishCategory> findSideDishCategoryByRepresentativeFoodName(String representativeFoodName);
}
