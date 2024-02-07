package com.example.thehealingmeal.menu.service;


import com.example.thehealingmeal.data.domain.MainDishCategory;
import com.example.thehealingmeal.data.domain.RiceCategory;
import com.example.thehealingmeal.data.domain.SideDishCategory;
import com.example.thehealingmeal.data.domain.SnackOrTeaCategory;
import com.example.thehealingmeal.data.repository.MainDishCategoryRepository;
import com.example.thehealingmeal.data.repository.RiceCategoryRepository;
import com.example.thehealingmeal.data.repository.SideDishCategoryRepository;
import com.example.thehealingmeal.data.repository.SnackOrTeaCategoryRepository;
import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.*;
import com.example.thehealingmeal.menu.domain.repository.MenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SideDishForUserMenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SnackOrTeaMenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SnackUrlRepository;
import com.example.thehealingmeal.survey.doamin.FilterFood;
import com.example.thehealingmeal.survey.doamin.Survey;
import com.example.thehealingmeal.survey.repository.FilterFoodRepository;
import com.example.thehealingmeal.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuGenerater {

    private final UserRepository userRepository;

    //식단 저장할 repositories
    private final MenuRepository menuRepository;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final SnackOrTeaMenuRepository snackOrTeaMenuRepository;


    //유저의 설문조사 테이블을 경유해서 필터링 키워드를 가져올 repository
    private final SurveyRepository surveyRepository;

    //필터링 키워드 repository
    private final FilterFoodRepository filterFoodRepository;

    //대표메뉴
    private final MainDishCategoryRepository mainDishCategoryRepository;

    //반찬
    private final SideDishCategoryRepository sideDishCategoryRepository;

    //밥
    private final RiceCategoryRepository riceCategoryRepository;

    //간식
    private final SnackOrTeaCategoryRepository snackOrTeaCategoryRepository;

    //간식 url
    private final SnackUrlRepository snackUrlRepository;

    //Google Cloud Storage Bucket Name
    @Value("${bucket-name}")
    private String bucket_name;

    //랜덤값 생성을 위한 객체
    SecureRandom secureRandom = new SecureRandom();


    //설문조사 결과(칼탄단지, 필터링 키워드)를 가지고 아침 간식 점심 간식 저녁
    //식단 생성(아침, 점심, 저녁만)
    public MenuResponseDto generateMenu(Meals meals, Long user_id) throws NoSuchElementException, IllegalArgumentException, NullPointerException{
        //식품 테이블에서 대표메뉴, 반찬, 밥을 랜덤하게 각각 가져옴. 단, 필터링을 적용함.
        //아래 코드는 난수를 생성하여 랜덤하게 식단을 가져오게 할 class

        /*
            대표메뉴 Main Dish
        */
        long recordCountForMain = mainDishCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수.

        Survey survey = surveyRepository.findByUserId(user_id); //유저의 설문조사 번호를 찾기 위한 변수
        FilterFood userFilter = filterFoodRepository.findFilterFoodBySurveyId(survey.getId()); //유저의 필터링 내용 가져오기

        //유저필터링 키워드를 콤마를 기준으로 리스트로 만들어줌.
        List<String> filterList = Arrays.asList((userFilter.getStewsAndHotpots() + "," + userFilter.getGrilledFood() + "," + userFilter.getPancakeFood()).split(","));

        //랜덤하게 대표메뉴 가져오고, null인지 필터링 키워드에 포함되는지 재차 확인함.
        Optional<MainDishCategory> optionalMainDish;
        do {
            optionalMainDish = mainDishCategoryRepository.findById(secureRandom.nextLong(recordCountForMain+1));
        } while (optionalMainDish.isEmpty() || filterList.contains(optionalMainDish.get().getRepresentativeFoodName()));
        MainDishCategory mainDishCategory = optionalMainDish.get();


        /*
            밥 Rice
         */
        long recordCountForRice = riceCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수


        Optional<RiceCategory> riceCategoryOptional = riceCategoryRepository.findById(secureRandom.nextLong(recordCountForRice + 1));
        while(riceCategoryOptional.isEmpty()) {
            riceCategoryOptional = riceCategoryRepository.findById(secureRandom.nextLong(recordCountForRice + 1));
        }
        RiceCategory riceCategory = riceCategoryOptional.get();
        /*
            반찬 2~3개 SideDishes random 2~3
         */
        long recordCountForSide = sideDishCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수
        int randomSideNumber = secureRandom.nextInt(2,4); //2~3개 중 몇 개의 반찬을 뽑을 것인지 정하는 변수
        //
        List<String> sideDishFilterList = Arrays.asList((userFilter.getVegetableFood() + "," + userFilter.getStirFriedFood() + "," + userFilter.getStewedFood()).split(","));

        //향후 결과값으로 반환할 땐 SideDishesDto 클래스로 할 것.
        //랜덤으로 정해진 반찬의 갯수만큼 반복
        List<SideDishCategory> sideDishCategories = new ArrayList<>();
        for (int start = 0; start < randomSideNumber; start++){
            Optional<SideDishCategory> optionalSideDish;
            SideDishCategory sideDishCategory;

            do {
                Long randomId = secureRandom.nextLong(recordCountForSide + 1);
                optionalSideDish = sideDishCategoryRepository.findById(randomId);
            } while (optionalSideDish.isEmpty() || sideDishFilterList.contains(optionalSideDish.get().getRepresentativeFoodName()) || sideDishCategories.contains(optionalSideDish.get()));

            sideDishCategory = optionalSideDish.get();
            sideDishCategories.add(sideDishCategory);
        }

        /*
            열탄단지 합산 Kcal, Protein, Carbohydrate, Fat Sum and User Info import
         */

        int kcal = mainDishCategory.getKcal() + riceCategory.getKcal() + sideDishCategories.stream().mapToInt(SideDishCategory::getKcal).sum();
        float protein = (float) (mainDishCategory.getProtein() + riceCategory.getProtein() + sideDishCategories.stream().mapToDouble(SideDishCategory::getProtein).sum());
        float carbohydrate = (float) (mainDishCategory.getCarbohydrate() + riceCategory.getCarbohydrate() + sideDishCategories.stream().mapToDouble(SideDishCategory::getCarbohydrate).sum());
        float fat = (float) (mainDishCategory.getFat() + riceCategory.getFat() + sideDishCategories.stream().mapToDouble(SideDishCategory::getFat).sum());

        User user = userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        //식단 반환 menu return
        return MenuResponseDto.builder()
                .main_dish(mainDishCategory.getRepresentativeFoodName())
                .imageURL("https://storage.googleapis.com/"+bucket_name+"/"+mainDishCategory.getRepresentativeFoodName()+".jpg")
                .sideDishForUserMenu(sideDishCategories.stream().map(SideDishCategory::getRepresentativeFoodName).collect(Collectors.toList()))
                .rice(riceCategory.getRepresentativeFoodName())
                .kcal(kcal)
                .protein(protein)
                .carbohydrate(carbohydrate)
                .fat(fat)
                .meals(meals)
                .user_id(user.getId())
                .user(user)
                .build();
    }

    //아점저 식단 저장
    public void saveMenu(MenuResponseDto menu){
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
        for (int i = 0; i<menu.getSideDishForUserMenu().size(); i++) {
            SideDishForUserMenu sideDishForUserMenu = SideDishForUserMenu.builder()
                    .side_dish(menu.getSideDishForUserMenu().get(i))
                    .menuForUser(menuForUser).build();
            sideDishForUserMenuRepository.save(sideDishForUserMenu);
        }
    }

    //간식 저장
    public void saveSnackOrTea(SnackOrTeaResponseDto snackOrTeaResponseDto){
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

    //간식 생성 아점-점저 사이만 허용 가능
    public SnackOrTeaResponseDto generateSnackOrTea(Meals meals,long user_id){
        long recordCountForSnackOrTea = snackOrTeaCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수.

        Survey survey = surveyRepository.findByUserId(user_id); //유저의 설문조사 번호를 찾기 위한 변수
        FilterFood userFilter = filterFoodRepository.findFilterFoodBySurveyId(survey.getId()); //유저의 필터링 내용 가져오기

        //콤마를 기준으로 필터링 키워드 리스트 작성
        List<String> filterList = Arrays.asList((userFilter.getBreadAndConfectionery()+","+userFilter.getDairyProducts()+","+userFilter.getBeveragesAndTeas()).split(","));

        Optional<SnackOrTeaCategory> optional;
        do {
            long randomId = secureRandom.nextLong(recordCountForSnackOrTea + 1);
            optional = snackOrTeaCategoryRepository.findById(randomId);
        } while (optional.isEmpty() || filterList.contains(optional.get().getRepresentativeFoodName()));
        SnackOrTeaCategory snackOrTeaCategory = optional.get();

        SnackURL snackURL = snackUrlRepository.findBySnackUrlNameStartingWith(snackOrTeaCategory.getRepresentativeFoodName());

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        return SnackOrTeaResponseDto.builder()
                .snack_or_tea(snackOrTeaCategory.getRepresentativeFoodName())
                .imageURL(snackURL.getSnackUrlName())
                .kcal(snackOrTeaCategory.getKcal())
                .carbohydrate(snackOrTeaCategory.getCarbohydrate())
                .protein(snackOrTeaCategory.getProtein())
                .fat(snackOrTeaCategory.getFat())
                .meals(meals)
                .userId(user_id)
                .user(user)
                .build();
    }

}
