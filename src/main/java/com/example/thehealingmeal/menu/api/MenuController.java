package com.example.thehealingmeal.menu.api;


import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.service.MenuManager;
import com.example.thehealingmeal.menu.service.MenuProvider;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuProvider menuProvider;
    private final MenuManager menuManager;

    //유저의 맞춤식단 생성
    @PostMapping("/{userId}/generate")
    public ResponseEntity<String> generateMenu(@PathVariable Long userId) {
        try {
            menuManager.generateForUser(userId);
            return new ResponseEntity<>("Menu Generated For User Successfully", HttpStatus.OK);
        } catch (InterruptedException | ExecutionException | java.util.concurrent.ExecutionException e) {
            return new ResponseEntity<>("Failed to Generate Menu For User", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMenu() {
        menuManager.resetMenu();
        return new ResponseEntity<>("Success to Reset Menu For User", HttpStatus.OK);
    }

    //유저별 식단 생성 확인
    @GetMapping("/{userId}/check")
    public ResponseEntity<Boolean> checkMenu(@PathVariable Long userId) {
        return new ResponseEntity<>(menuManager.checkMenu(userId), HttpStatus.OK);
    }
}

