package com.example.thehealingmeal.menu.service;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.menu.api.dto.BookmarkRequestDto;
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
    public void createMenuBookmark(Long userId, BookmarkRequestDto bookmarkRequestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        MenuForUser menuForUser = menuRepository.findByUserAndMeals(user, bookmarkRequestDto.getMeals());
        if (menuForUser == null) {
            throw new IllegalArgumentException("MenuForUser not found for the given user and meals");
        }
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .menuForUserId(menuForUser.getId())
                .build();
        bookmarkRepository.save(bookmark);
    }

    // 아점저 메뉴 즐겨찾기 조회
    public List<MenuResponseDto> menuBookmarkList(Long userId) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByUserId(userId);

        List<MenuResponseDto> menuResponseDtos = new ArrayList<>(); // 반환할 값

        for (Bookmark bookmark : bookmarkList) {
            List<SideDishForUserMenu> sideMenus = sideDishForUserMenuRepository.findAllByMenuForUser_Id(bookmark.getMenuForUserId());
            List<String> sideDishNames = sideMenus.stream()
                    .map(SideDishForUserMenu::getSide_dish)
                    .toList();
            MenuForUser menuForUser = menuRepository.findById(userId).orElseThrow();
            MenuResponseDto menuResponseDto = MenuResponseDto.createMenu(
                    bookmark.getId(),
                    menuForUser.getMain_dish(),
                    "https://storage.googleapis.com/" + bucket_name + "/" + menuForUser.getMain_dish() + ".jpg",
                    menuForUser.getRice(),
                    menuForUser.getMeals(),
                    sideDishNames,
                    menuForUser.getKcal(),
                    menuForUser.getProtein(),
                    menuForUser.getCarbohydrate(),
                    menuForUser.getFat()
            );
            menuResponseDtos.add(menuResponseDto);
        }

        return menuResponseDtos;
    }

    // 아점저 메뉴 즐겨찾기 삭제.
    @Transactional
    public void deleteMenuBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow();
        bookmarkRepository.delete(bookmark);
    }

     //간식 메뉴 즐겨찾기 저장.
    @Transactional
    public void createSnackBookmark(Long userId, BookmarkRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        SnackOrTea snackOrTea = snackOrTeaMenuRepository.findByUserAndMeals(user, requestDto.getMeals());
        if (snackOrTea == null) {
            throw new IllegalArgumentException("SnackOrTea not found for the given user and meals");
        }
        SnackBookmark snackBookmark = SnackBookmark.builder()
                .user(user)
                .snackOrTeaId(snackOrTea.getId())
                .build();
        snackBookmarkRepository.save(snackBookmark);
    }
//
//    // 간식 메뉴 즐겨찾기 조회
    public List<SnackOrTeaResponseDto> snackBookmarkList(Long userId) {
        List<SnackBookmark> snackBookmarkList = snackBookmarkRepository.findByUserId(userId);

        List<SnackOrTeaResponseDto> snackOrTeaResponseDtos = new ArrayList<>(); // 반환할 값


        for (SnackBookmark snackBookmark : snackBookmarkList) {
            SnackOrTea snackOrTea = snackOrTeaMenuRepository.findById(userId).orElseThrow();
            SnackOrTeaResponseDto snackOrTeaResponseDto = SnackOrTeaResponseDto.createMenu(
                    snackBookmark.getId(),
                    snackOrTea.getSnack_or_tea(),
                    "https://storage.googleapis.com/" + bucket_name + "/" + snackOrTea.getImageUrl(),
                    snackOrTea.getMeals(),
                    snackOrTea.getKcal(),
                    snackOrTea.getProtein(),
                    snackOrTea.getCarbohydrate(),
                    snackOrTea.getFat()
            );
            snackOrTeaResponseDtos.add(snackOrTeaResponseDto);
        }
        return snackOrTeaResponseDtos;
    }
    // 간식 메뉴 즐겨찾기 삭제.
    @Transactional
    public void deleteSnackBookmark(Long snackBookmarkId) {
        SnackBookmark snackBookmark = snackBookmarkRepository.findById(snackBookmarkId).orElseThrow();
        snackBookmarkRepository.delete(snackBookmark);
    }
}