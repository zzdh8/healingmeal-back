package com.example.thehealingmeal.menu.domain.repository;

import com.example.thehealingmeal.menu.domain.SideDishForUserMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SideDishForUserMenuRepository extends JpaRepository<SideDishForUserMenu, Long> {
    int countByMenuForUser_Id(long menu_id);
    List<SideDishForUserMenu> findAllByMenuForUser_Id(long menu_id);
}
