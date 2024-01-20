package com.example.thehealingmeal.data.repository;

import com.example.thehealingmeal.data.domain.MainDishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainDishCategoryRepository extends JpaRepository<MainDishCategory, Long> {
    Optional<MainDishCategory> findById(long user_id);
}
