package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.menu.domain.SnackURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnackUrlRepository extends JpaRepository<SnackURL, Long> {
    SnackURL findBySnackUrlNameStartingWith(String name);
}
