package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.menu.domain.Bookmark;
import com.example.thehealingmeal.menu.domain.SnackBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SnackBookmarkRepository extends JpaRepository<SnackBookmark, Long> {
    List<SnackBookmark> findByUserId(Long userId);
}
