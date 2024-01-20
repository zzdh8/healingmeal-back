package com.example.thehealingmeal.menu.api;


import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.service.MenuProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuContoller {
    private final MenuProvider menuProvider;
    

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
}

