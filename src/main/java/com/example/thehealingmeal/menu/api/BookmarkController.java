package com.example.thehealingmeal.menu.api;

import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // 아점저 즐겨찾기 추가
    @PostMapping("/{userId}/bookmark")
    public ResponseEntity<String> bookmark(@PathVariable Long userId, @RequestBody Meals meals) {
        bookmarkService.createMenuBookmark(userId, meals);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }

    // 아점저 즐겨찾기 확인
    @GetMapping("/{userId}/bookmark")
    public ResponseEntity<List<MenuResponseDto>> bookmarkList(@PathVariable Long userId) {
        return new ResponseEntity<>(bookmarkService.menuBookmarkList(userId), HttpStatus.OK);
    }

    // 아점저 즐겨찾기 삭제
    @DeleteMapping("/{bookmarkId}/bookmark")
    public ResponseEntity<String> deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.deleteMenuBookmark(bookmarkId);
        return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    }

    //간식 즐겨찾기 추가
    @PostMapping("/{userId}/snack/bookmark")
    public ResponseEntity<String> snackBookmark(@PathVariable Long userId, @RequestBody Meals meals) {
        bookmarkService.createSnackBookmark(userId, meals);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }

    // 간식 즐겨찾기 확인
    @GetMapping("/{userId}/snack/bookmark")
    public ResponseEntity<List<SnackOrTeaResponseDto>> snackBookmarkList(@PathVariable Long userId) {
        return new ResponseEntity<>(bookmarkService.snackBookmarkList(userId), HttpStatus.OK);
    }

    // 간식 즐겨찾기 삭제
    @DeleteMapping("/{snackBookmarkId}/snack/bookmark")
    public ResponseEntity<String> deleteSnackBookmark(@PathVariable Long snackBookmarkId) {
        bookmarkService.deleteSnackBookmark(snackBookmarkId);
        return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    }
}
