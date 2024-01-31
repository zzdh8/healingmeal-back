package com.example.thehealingmeal.menu.api;


import com.example.thehealingmeal.data.repository.SideDishCategoryRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.SideDishForUserMenu;
import com.example.thehealingmeal.menu.domain.repository.SideDishForUserMenuRepository;
import com.example.thehealingmeal.menu.service.BookmarkService;
import com.example.thehealingmeal.menu.service.MenuProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
public class MenuContoller {
    private final MenuProvider menuProvider;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final BookmarkService bookmarkService;

    //유저의 맞춤식단 생성
    @PostMapping("/{userId}/generate")
    public ResponseEntity<String> generateMenu(@PathVariable Long userId) {
        menuProvider.generateForUser(userId);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }

    //아침 식단 제공
    @GetMapping("/{userId}/provide/breakfast")
    public ResponseEntity<MenuResponseDto> breakfast(@PathVariable Long userId) {
        MenuResponseDto menuResponseDto = menuProvider.provide(userId, Meals.BREAKFAST);
        return new ResponseEntity<>(menuResponseDto, HttpStatus.OK);
    }

    //점심 식단 제공
    @GetMapping("/{userId}/provide/lunch")
    public ResponseEntity<MenuResponseDto> lunch(@PathVariable Long userId) {
        MenuResponseDto menuResponseDto = menuProvider.provide(userId, Meals.LUNCH);
        return new ResponseEntity<>(menuResponseDto, HttpStatus.OK);
    }

    //저녁 식단 제공
    @GetMapping("/{userId}/provide/dinner")
    public ResponseEntity<MenuResponseDto> dinner(@PathVariable Long userId) {
        MenuResponseDto menuResponseDto = menuProvider.provide(userId, Meals.DINNER);
        return new ResponseEntity<>(menuResponseDto, HttpStatus.OK);
    }

    //아침-점심 간식 제공
    @GetMapping("/{userId}/provide/breakfast-snack-or-tea")
    public ResponseEntity<SnackOrTeaResponseDto> breakfastSnackOrTea(@PathVariable Long userId) {
        SnackOrTeaResponseDto snackOrTeaResponseDto = menuProvider.provideSnackOrTea(userId, Meals.BREAKFAST_SNACKORTEA);
        return new ResponseEntity<>(snackOrTeaResponseDto, HttpStatus.OK);
    }

    //점심-저녁 간식 제공
    @GetMapping("/{userId}/provide/lunch-snack-or-tea")
    public ResponseEntity<SnackOrTeaResponseDto> lunchSnackOrTea(@PathVariable Long userId) {
        SnackOrTeaResponseDto snackOrTeaResponseDto = menuProvider.provideSnackOrTea(userId, Meals.LUNCH_SNACKORTEA);
        return new ResponseEntity<>(snackOrTeaResponseDto, HttpStatus.OK);
    }

    //00시 식단 초기화
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<String> deleteMenu(@PathVariable Long userId) {
        try {
            menuProvider.resetMenu(userId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Incorrect User ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Success to Reset Menu For User", HttpStatus.OK);
    }

    // 아점저 즐겨찾기 추가
    @PostMapping("/{userId}/bookmark")
    public ResponseEntity<String> bookmark(@PathVariable Long userId, @RequestBody Meals meals){
        bookmarkService.createMenuBookmark(userId, meals);
        return new ResponseEntity<>("성공",HttpStatus.OK);
    }
    // 아점저 즐겨찾기 확인
    @GetMapping("/{userId}/bookmark")
    public ResponseEntity<List<MenuResponseDto>> bookmarkList(@PathVariable Long userId){
     return new ResponseEntity<>(bookmarkService.menuBookmarkList(userId),HttpStatus.OK);
    }
     //간식 즐겨찾기 추가
    @PostMapping("/{userId}/snack/bookmark")
    public ResponseEntity<String> snackBookmark(@PathVariable Long userId, @RequestBody Meals meals){
        bookmarkService.createSnackBookmark(userId,meals);
        return new ResponseEntity<>("성공",HttpStatus.OK);
    }
//    // 간식 즐겨찾기 확인
    @GetMapping("/{userId}/snack/bookmark")
    public ResponseEntity<List<SnackOrTeaResponseDto>> snackBookmarkList(@PathVariable Long userId){
        return new ResponseEntity<>(bookmarkService.snackBookmarkList(userId),HttpStatus.OK);
    }
}

