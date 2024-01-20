package com.example.thehealingmeal.menu.service;


import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.MenuForUser;
import com.example.thehealingmeal.menu.domain.SideDishForUserMenu;
import com.example.thehealingmeal.menu.domain.SnackOrTea;
import com.example.thehealingmeal.menu.domain.repository.MenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SideDishForUserMenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SnackOrTeaMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuProvider {
    private final MenuGenerater menuGenerater;
    private final MenuRepository menuRepository;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final SnackOrTeaMenuRepository snackOrTeaMenuRepository;

    @Value("${bucket-name}")
    private String bucket_name;

    //유저를 위한 전체 식단 생성 및 유효 검사 메소드
    @Transactional
    public void generateForUser(long user_id){
        Meals[] meals = Meals.values();
        MenuResponseDto[] menus = new MenuResponseDto[meals.length-2];
        SnackOrTeaResponseDto[] snackOrTeas = new SnackOrTeaResponseDto[meals.length-3];

        do {
            for (int i = 0; i < meals.length; i++) {
                if (i < 3) {
                    menus[i] = menuGenerater.generateMenu(meals[i], user_id);
                } else {
                    snackOrTeas[i-3] = menuGenerater.generateSnackOrTea(meals[i], user_id);
                }
            }
        } while (menuGenerater.isExceed(user_id, menus[0], snackOrTeas[0], menus[1], snackOrTeas[1], menus[2]));

        for (MenuResponseDto dto : menus){
            menuGenerater.saveMenu(dto);
        }
        for (SnackOrTeaResponseDto dto : snackOrTeas){
            menuGenerater.saveSnackOrTea(dto);
        }
    }

    //아침, 점심, 저녁 식단 제공 메소드
    public MenuResponseDto provide(long usr_id, Meals meals){
        MenuForUser menu = menuRepository.findByUserIdAndMeals(usr_id, meals);
        List<SideDishForUserMenu> sideMenus = sideDishForUserMenuRepository.findAllByMenuForUser_Id(menu.getId());
        List<String> sideDishNames = sideMenus.stream()
                .map(SideDishForUserMenu::getSide_dish)
                .collect(Collectors.toList());
        return MenuResponseDto.builder()
                .main_dish(menu.getMain_dish())
                .imageURL("https://storage.googleapis.com/"+bucket_name+"/"+menu.getMain_dish()+".jpg")
                .kcal(menu.getKcal())
                .rice(menu.getRice())
                .fat(menu.getFat())
                .protein(menu.getProtein())
                .carbohydrate(menu.getCarbohydrate())
                .meals(menu.getMeals())
                .user_id(menu.getUser().getId())
                .sideDishForUserMenu(sideDishNames)
                .build();
    }

    //아점, 점저 간식 메공 메소드
    public SnackOrTeaResponseDto provideSnackOrTea(long user_id, Meals meals){
        SnackOrTea snackOrTea = snackOrTeaMenuRepository.findByUserIdAndMeals(user_id, meals);
        return SnackOrTeaResponseDto.builder()
                .snack_or_tea(snackOrTea.getSnack_or_tea())
                .meals(snackOrTea.getMeals())
                .imageURL("https://storage.googleapis.com/"+bucket_name+"/"+snackOrTea.getImageUrl())
                .kcal(snackOrTea.getKcal())
                .carbohydrate(snackOrTea.getCarbohydrate())
                .protein(snackOrTea.getProtein())
                .fat(snackOrTea.getFat())
                .user(snackOrTea.getUser())
                .userId(snackOrTea.getUser().getId())
                .build();
    }
}
