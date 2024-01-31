package com.example.thehealingmeal.menu.service;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.*;
import com.example.thehealingmeal.menu.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final SnackOrTeaMenuRepository snackOrTeaMenuRepository;
    private final SnackBookmarkRepository snackBookmarkRepository;

    @Value("${bucket-name}")
    private String bucket_name;

    // 아점저 메뉴 즐겨찾기 저장
    @Transactional
    public void createMenuBookmark(Long userId, Meals meals) {
        User user = userRepository.findById(userId).orElseThrow();
        MenuForUser menuForUser = menuRepository.findByUserAndMeals(user, meals);
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .menuForUser(menuForUser)
                .build();
        bookmarkRepository.save(bookmark);
    }

    // 아점저 메뉴 즐겨찾기 조회
    public List<MenuResponseDto> menuBookmarkList(Long userId) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByUserId(userId);

        List<MenuResponseDto> menuResponseDtos = new ArrayList<>(); // 반환할 값


        for (Bookmark bookmark : bookmarkList) {
            List<SideDishForUserMenu> sideMenus = sideDishForUserMenuRepository.findAllByMenuForUser_Id(bookmark.getMenuForUser().getId());
            List<String> sideDishNames = sideMenus.stream()
                    .map(SideDishForUserMenu::getSide_dish)
                    .toList();
            MenuResponseDto menuResponseDto = MenuResponseDto.createMenu(
                    bookmark.getMenuForUser().getMain_dish(),
                    "https://storage.googleapis.com/" + bucket_name + "/" + bookmark.getMenuForUser().getMain_dish() + ".jpg",
                    bookmark.getMenuForUser().getRice(),
                    bookmark.getMenuForUser().getMeals(),
                    sideDishNames,
                    bookmark.getMenuForUser().getKcal(),
                    bookmark.getMenuForUser().getProtein(),
                    bookmark.getMenuForUser().getCarbohydrate(),
                    bookmark.getMenuForUser().getFat()
            );
            menuResponseDtos.add(menuResponseDto);
        }

        return menuResponseDtos;
    }

     //간식 메뉴 즐겨찾기 저장.
    @Transactional
    public void createSnackBookmark(Long userId, Meals meals) {
        User user = userRepository.findById(userId).orElseThrow();
        SnackOrTea snackOrTea = snackOrTeaMenuRepository.findByUserAndMeals(user, meals);
        SnackBookmark snackBookmark = SnackBookmark.builder()
                .user(user)
                .snackOrTea(snackOrTea)
                .build();
        snackBookmarkRepository.save(snackBookmark);
    }
//
//    // 간식 메뉴 즐겨찾기 조회
    public List<SnackOrTeaResponseDto> snackBookmarkList(Long userId) {
        List<SnackBookmark> snackBookmarkList = snackBookmarkRepository.findByUserId(userId);

        List<SnackOrTeaResponseDto> snackOrTeaResponseDtos = new ArrayList<>(); // 반환할 값


        for (SnackBookmark snackBookmark : snackBookmarkList) {
            SnackOrTeaResponseDto snackOrTeaResponseDto = SnackOrTeaResponseDto.createMenu(
                    snackBookmark.getSnackOrTea().getSnack_or_tea(),
                    "https://storage.googleapis.com/" + bucket_name + "/" + snackBookmark.getSnackOrTea().getImageUrl(),
                    snackBookmark.getSnackOrTea().getMeals(),
                    snackBookmark.getSnackOrTea().getKcal(),
                    snackBookmark.getSnackOrTea().getProtein(),
                    snackBookmark.getSnackOrTea().getCarbohydrate(),
                    snackBookmark.getSnackOrTea().getFat()
            );
            snackOrTeaResponseDtos.add(snackOrTeaResponseDto);
        }
        return snackOrTeaResponseDtos;
    }
}