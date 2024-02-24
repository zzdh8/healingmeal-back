package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Bookmark;
import com.example.thehealingmeal.menu.domain.Meals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(Long userId);


    @Query("SELECT b FROM Bookmark b WHERE b.main_dish = :main_dish AND b.rice = :rice AND b.meals = :meals AND b.user = :user")
    Bookmark findDuplicateValues(@Param("main_dish") String main_dish,
                                 @Param("rice") String rice,
                                 @Param("meals") Meals meals,
                                 @Param("user") User user);
}
