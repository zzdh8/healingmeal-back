package com.example.thehealingmeal.data.repository;

import com.example.thehealingmeal.data.domain.SideDishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SideDishCategoryRepository extends JpaRepository<SideDishCategory, Long> {
}
