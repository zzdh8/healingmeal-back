package com.example.thehealingmeal.survey.repository;


import com.example.thehealingmeal.survey.domain.FilterFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterFoodRepository extends JpaRepository<FilterFood, Long> {
    FilterFood findFilterFoodByUserId(Long userId);
}
