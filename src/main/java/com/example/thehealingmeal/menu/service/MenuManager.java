package com.example.thehealingmeal.menu.service;

import com.example.thehealingmeal.data.repository.MainDishCategoryRepository;
import com.example.thehealingmeal.data.repository.RiceCategoryRepository;
import com.example.thehealingmeal.data.repository.SideDishCategoryRepository;
import com.example.thehealingmeal.data.repository.SnackOrTeaCategoryRepository;
import com.example.thehealingmeal.gpt.responseRepository.ResponseRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.*;
import com.example.thehealingmeal.menu.domain.repository.MenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SideDishForUserMenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SnackOrTeaMenuRepository;
import com.example.thehealingmeal.survey.domain.SurveyResult;
import com.example.thehealingmeal.survey.repository.SurveyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MenuManager {
    private final SurveyResultRepository surveyResultRepository;
    private final MenuRepository menuRepository;
    private final MainDishCategoryRepository mainDishCategoryRepository;
    private final SideDishCategoryRepository sideDishCategoryRepository;
    private final RiceCategoryRepository riceCategoryRepository;
    private final SnackOrTeaCategoryRepository snackOrTeaCategoryRepository;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final SnackOrTeaMenuRepository snackOrTeaMenuRepository;
    private final ResponseRepository responseRepository;
    private final MenuGenerater menuGenerater;


    @Transactional
    public void generateForUser(long user_id) throws InterruptedException, ExecutionException {
        Meals[] meals = Meals.values();
        List<Long> mainList = menuGenerater.generateRandomNumbers(mainDishCategoryRepository.count());
        List<Long> sideList = menuGenerater.generateRandomNumbers(sideDishCategoryRepository.count());
        List<Long> riceList = menuGenerater.generateRandomNumbers(riceCategoryRepository.count());
        List<Long> snackOrTeaList = menuGenerater.generateRandomNumbers(snackOrTeaCategoryRepository.count());

        CompletableFuture<MenuResponseDto> breakfast = menuGenerater.generateMenu(meals[0], user_id, mainList, sideList, riceList);
        CompletableFuture<MenuResponseDto> lunch = menuGenerater.generateMenu(meals[1], user_id, mainList, sideList, riceList);
        CompletableFuture<MenuResponseDto> dinner = menuGenerater.generateMenu(meals[2], user_id, mainList, sideList, riceList);
        CompletableFuture<SnackOrTeaResponseDto> breakfastSOT = menuGenerater.generateSnackOrTea(meals[3], user_id, snackOrTeaList);
        CompletableFuture<SnackOrTeaResponseDto> lunchSOT = menuGenerater.generateSnackOrTea(meals[4], user_id, snackOrTeaList);

        CompletableFuture.allOf(breakfast, lunch, dinner, breakfastSOT, lunchSOT).join();
        while (isExceed(user_id, breakfast.get(), breakfastSOT.get(), lunch.get(), lunchSOT.get(), dinner.get())) {
            ExceedInfo exceedInfo = whatIsExceed(user_id, breakfast.get(), breakfastSOT.get(), lunch.get(), lunchSOT.get(), dinner.get());
            if (exceedInfo.getKcalExceed() == 0 && exceedInfo.getCarbohydrateExceed() == 0 && exceedInfo.getProteinExceed() == 0 && exceedInfo.getFatExceed() == 0) {
                break;
            } else if (exceedInfo.getMeals().equals(Meals.BREAKFAST)) {
                breakfast = menuGenerater.reloadMenu(user_id, breakfast.get(), exceedInfo);
            } else if (exceedInfo.getMeals().equals(Meals.LUNCH)) {
                lunch = menuGenerater.reloadMenu(user_id, lunch.get(), exceedInfo);
            } else {
                dinner = menuGenerater.reloadMenu(user_id, dinner.get(), exceedInfo);
            }
            CompletableFuture.allOf(breakfast, lunch, dinner).join();
        }

        saveMenu(breakfast.get());
        saveMenu(lunch.get());
        saveMenu(dinner.get());
        saveSnackOrTea(breakfastSOT.get());
        saveSnackOrTea(lunchSOT.get());
    }


    //생성된 식단의 유효성을 검사하는 메소드
    public boolean isExceed(Long user_id, MenuResponseDto morning,
                            SnackOrTeaResponseDto morningSnackOrTea,
                            MenuResponseDto lunch,
                            SnackOrTeaResponseDto lunchSnackOrTea,
                            MenuResponseDto dinner) {
        //유저의 설문조사 결과치를 가져옴. 이 결과치를 초과하지 않도록 함.
        SurveyResult surveyResult = surveyResultRepository.findByUserId(user_id);
        Nutrient totalNutrients = morning.getTotalNutrients()
                .add(morningSnackOrTea.getTotalNutrients())
                .add(lunch.getTotalNutrients())
                .add(lunchSnackOrTea.getTotalNutrients())
                .add(dinner.getTotalNutrients());
        //식단의 열탄단지 합이 설문조사한 유저의 하루필요량을 하나라도 넘으면 true 아니면, false 반환
        return surveyResult.exceeds(totalNutrients);
    }

    public ExceedInfo whatIsExceed(Long user_id, MenuResponseDto morning,
                                   SnackOrTeaResponseDto morningSnackOrTea,
                                   MenuResponseDto lunch,
                                   SnackOrTeaResponseDto lunchSnackOrTea,
                                   MenuResponseDto dinner) {
        //유저의 설문조사 결과치 = 대조 기준
        SurveyResult surveyResult = surveyResultRepository.findByUserId(user_id);
        List<Nutrient> nutrients = Arrays.asList(
                morning.getTotalNutrients(),
                lunch.getTotalNutrients(),
                dinner.getTotalNutrients(),
                morningSnackOrTea.getTotalNutrients(),
                lunchSnackOrTea.getTotalNutrients()
        );
        Nutrient totalNutrients = nutrients.stream().reduce(Nutrient::add).orElseThrow(() ->
                new NoSuchElementException("No Such Element Error : nutrients"));
        System.out.println(totalNutrients.getKcal() + " "+ totalNutrients.getFat()+" "+ totalNutrients.getCarbohydrate()+" "+ totalNutrients.getProtein());
        System.out.println((totalNutrients.getKcal() - surveyResult.getKcal() <= 100 ? 0 : totalNutrients.getKcal() - surveyResult.getKcal())+" "+ (totalNutrients.getCarbohydrate() - surveyResult.getCarbohydrate() < 0 ? 0 : totalNutrients.getCarbohydrate() - surveyResult.getCarbohydrate()) + " "+
                (totalNutrients.getCarbohydrate() - surveyResult.getCarbohydrate() <= 20 ? 0 : totalNutrients.getCarbohydrate() - surveyResult.getCarbohydrate()) + " " +(totalNutrients.getProtein() - surveyResult.getProtein() <= 50 ? 0 : totalNutrients.getProtein() - surveyResult.getProtein()) + " "
                + (totalNutrients.getFat() - surveyResult.getFat() <= 20 ? 0 : totalNutrients.getFat() - surveyResult.getFat()));
        Nutrient maxNutrient = Collections.max(nutrients, Comparator.comparing(Nutrient::getKcal));
        return ExceedInfo.builder()
                .meals(maxNutrient.getMeals())
                .kcalExceed(totalNutrients.getKcal() - surveyResult.getKcal() <= 100 ? 0 : totalNutrients.getKcal() - surveyResult.getKcal())
                .carbohydrateExceed(totalNutrients.getCarbohydrate() - surveyResult.getCarbohydrate() <= 20 ? 0 : totalNutrients.getCarbohydrate() - surveyResult.getCarbohydrate())
                .proteinExceed(totalNutrients.getProtein() - surveyResult.getProtein() <= 50 ? 0 : totalNutrients.getProtein() - surveyResult.getProtein())
                .fatExceed(totalNutrients.getFat() - surveyResult.getFat() <= 20 ? 0 : totalNutrients.getFat() - surveyResult.getFat())
                .build();
    }

    @Transactional
    public void saveMenu(MenuResponseDto menu) {
        MenuForUser menuForUser = MenuForUser.builder()
                .main_dish(menu.getMain_dish())
                .rice(menu.getRice())
                .kcal(menu.getKcal())
                .protein(menu.getProtein())
                .carbohydrate(menu.getCarbohydrate())
                .fat(menu.getFat())
                .meals(menu.getMeals())
                .user(menu.getUser())
                .build();
        menuRepository.save(menuForUser);
        for (int i = 0; i < menu.getSideDishForUserMenu().size(); i++) {
            SideDishForUserMenu sideDishForUserMenu = SideDishForUserMenu.builder()
                    .side_dish(menu.getSideDishForUserMenu().get(i))
                    .menuForUser(menuForUser).build();
            sideDishForUserMenuRepository.save(sideDishForUserMenu);
        }
    }

    //간식 저장
    @Transactional
    public void saveSnackOrTea(SnackOrTeaResponseDto snackOrTeaResponseDto) {
        SnackOrTea snackOrTea = SnackOrTea.builder()
                .snack_or_tea(snackOrTeaResponseDto.getSnack_or_tea())
                .user(snackOrTeaResponseDto.getUser())
                .carbohydrate(snackOrTeaResponseDto.getCarbohydrate())
                .fat(snackOrTeaResponseDto.getFat())
                .protein(snackOrTeaResponseDto.getProtein())
                .kcal(snackOrTeaResponseDto.getKcal())
                .meals(snackOrTeaResponseDto.getMeals())
                .imageUrl(snackOrTeaResponseDto.getImageURL())
                .build();
        snackOrTeaMenuRepository.save(snackOrTea);
    }

    //오전 00시 유저 식단 초기화 메소드
    @Transactional
    public void resetMenu() throws RuntimeException {
        menuRepository.deleteAll();
        sideDishForUserMenuRepository.deleteAll();
        snackOrTeaMenuRepository.deleteAll();
        responseRepository.deleteAll();
    }

    //식단 확인
    public boolean checkMenu(long user_id) throws NoSuchElementException {
        try {
            return menuRepository.existsByUserId(user_id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Such Element Error : please check user_id");
        }
    }
}
