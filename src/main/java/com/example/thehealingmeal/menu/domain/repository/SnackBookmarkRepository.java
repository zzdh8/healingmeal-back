package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Bookmark;
import com.example.thehealingmeal.menu.domain.SnackBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SnackBookmarkRepository extends JpaRepository<SnackBookmark, Long> {
    List<SnackBookmark> findByUserId(Long userId);

    @Query("SELECT sb FROM SnackBookmark sb WHERE sb.user = :user AND sb.snackOrTeaId = :snackOrTeaId")
    SnackBookmark findByUserAndSnackOrTeaId(@Param("user") User user, @Param("snackOrTeaId") Long snackOrTeaId);
}
