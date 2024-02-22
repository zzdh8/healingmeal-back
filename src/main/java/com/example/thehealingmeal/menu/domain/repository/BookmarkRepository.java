package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(Long userId);

    Bookmark findByUserAndMenuForUserId(User user, Long id);
}
