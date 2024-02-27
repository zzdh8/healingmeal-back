package com.example.thehealingmeal.data.repository;

import com.example.thehealingmeal.data.domain.RiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiceCategoryRepository extends JpaRepository<RiceCategory, Long> {
    Optional<RiceCategory> findById(long id);
    RiceCategory findByRepresentativeFoodName(String representativeFoodName);
}
