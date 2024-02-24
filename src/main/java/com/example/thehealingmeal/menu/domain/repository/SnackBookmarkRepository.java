package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Bookmark;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.SnackBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SnackBookmarkRepository extends JpaRepository<SnackBookmark, Long> {
    List<SnackBookmark> findByUserId(Long userId);

    @Query("SELECT s FROM SnackBookmark s WHERE s.snack_or_tea = :snack_or_tea AND s.meals = :meals")
    SnackBookmark findDuplicateValues(@Param("snack_or_tea") String snack_or_tea,
                                            @Param("meals") Meals meals);
}
