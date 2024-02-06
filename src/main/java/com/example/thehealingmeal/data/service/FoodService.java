package com.example.thehealingmeal.data.service;


import com.example.thehealingmeal.data.domain.*;
import com.example.thehealingmeal.data.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final RiceCategoryRepository riceCategoryRepository;
    private final SideDishCategoryRepository sideDishCategoryRepository;
    private final SnackOrTeaCategoryRepository snackCategoryRepository;
    private final MainDishCategoryRepository mainDishCategoryRepository;


    @Transactional
    public void loadSave() throws Exception {
        JSONParser parser = new JSONParser();
        ClassPathResource resource = new ClassPathResource("food-data.json");
        JSONArray records = (JSONArray) parser.parse(new InputStreamReader(resource.getInputStream(), "UTF-8"));

        for (Object record : records) {
            try {

                JSONObject tmp = (JSONObject) record;
                Food food = Food.builder()
                        .foodName(tmp.get("식품명").toString())
                        .foodCategory(tmp.get("식품대분류명").toString())
                        .representativeFoodName(tmp.get("대표식품명").toString())
                        .Kcal(Integer.parseInt(tmp.get("에너지(kcal)").toString()))
                        .protein(Float.parseFloat(tmp.get("단백질(g)").toString()))
                        .fat(Float.parseFloat(tmp.get("지방(g)").toString()))
                        .carbohydrate(Float.parseFloat(tmp.get("탄수화물(g)").toString()))
                        .sugar(Float.parseFloat(tmp.get("당류(g)").toString()))
                        .sodium(Integer.parseInt(tmp.get("나트륨(mg)").toString()))
                        .build();

                foodRepository.save(food);
            } catch (Exception e) {
                System.err.println("ClassCastException 발생: " + e.getMessage());
            }
        }

    }
    @Transactional
    public void saveRiceCategoryFoods() {
        List<Food> riceFoods = foodRepository.findByFoodCategory("밥류");

        for (Food food : riceFoods) {
            RiceCategory riceCategory = RiceCategory.builder()
                    .foodName(food.getFoodName())
                    .foodCategory(food.getFoodCategory())
                    .representativeFoodName(food.getRepresentativeFoodName())
                    .Kcal(food.getKcal())
                    .protein(food.getProtein())
                    .fat(food.getFat())
                    .carbohydrate(food.getCarbohydrate())
                    .sugar(food.getSugar())
                    .sodium(food.getSodium())
                    .build();

            riceCategoryRepository.save(riceCategory);
        }
    }
    @Transactional
    public void saveSideDishCategoryFoods() {
        List<Food> sideDishFoods = new ArrayList<>();
        sideDishFoods.addAll(foodRepository.findByFoodCategory("나물·숙채류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("조림류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("볶음류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("김치류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("젓갈류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("장아찌·절임류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("수·조·어·육류"));
        sideDishFoods.addAll(foodRepository.findByFoodCategory("생채·무침류"));
        for (Food food : sideDishFoods) {
            SideDishCategory sideDishCategory = SideDishCategory.builder()
                    .foodName(food.getFoodName())
                    .foodCategory(food.getFoodCategory())
                    .representativeFoodName(food.getRepresentativeFoodName())
                    .Kcal(food.getKcal())
                    .protein(food.getProtein())
                    .fat(food.getFat())
                    .carbohydrate(food.getCarbohydrate())
                    .sugar(food.getSugar())
                    .sodium(food.getSodium())
                    .build();
            sideDishCategoryRepository.save(sideDishCategory);
        }
    }
    @Transactional
    public void saveSnackOrTeaCategoryFoods() {
        List<Food> snackOrTeaFoods = new ArrayList<>();
        snackOrTeaFoods.addAll(foodRepository.findByFoodCategory("유제품류"));
        snackOrTeaFoods.addAll(foodRepository.findByFoodCategory("음료 및 차류"));
        snackOrTeaFoods.addAll(foodRepository.findByFoodCategory("빵 및 과자류"));

        for (Food food : snackOrTeaFoods) {
            SnackOrTeaCategory snackOrTeaCategory = SnackOrTeaCategory.builder()
                    .foodName(food.getFoodName())
                    .foodCategory(food.getFoodCategory())
                    .representativeFoodName(food.getRepresentativeFoodName())
                    .Kcal(food.getKcal())
                    .protein(food.getProtein())
                    .fat(food.getFat())
                    .carbohydrate(food.getCarbohydrate())
                    .sugar(food.getSugar())
                    .sodium(food.getSodium())
                    .build();
            snackCategoryRepository.save(snackOrTeaCategory);
        }

    }
    @Transactional
    public void saveMainDishCategoryFoods() {
        List<Food> mainDishFoods = new ArrayList<>();
        mainDishFoods.addAll(foodRepository.findByFoodCategory("구이류"));
        mainDishFoods.addAll(foodRepository.findByFoodCategory("찌개 및 전골류"));
        mainDishFoods.addAll(foodRepository.findByFoodCategory("전·적 및 부침류"));
        mainDishFoods.addAll(foodRepository.findByFoodCategory("찜류"));
        for (Food food : mainDishFoods) {
            MainDishCategory mainDishCategory = MainDishCategory.builder()
                    .foodName(food.getFoodName())
                    .foodCategory(food.getFoodCategory())
                    .representativeFoodName(food.getRepresentativeFoodName())
                    .Kcal(food.getKcal())
                    .protein(food.getProtein())
                    .fat(food.getFat())
                    .carbohydrate(food.getCarbohydrate())
                    .sugar(food.getSugar())
                    .sodium(food.getSodium())
                    .build();
            mainDishCategoryRepository.save(mainDishCategory);
        }
    }
}
