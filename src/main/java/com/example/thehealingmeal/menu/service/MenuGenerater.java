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
import com.example.thehealingmeal.survey.doamin.SurveyResult;
import com.example.thehealingmeal.survey.repository.FilterFoodRepository;
import com.example.thehealingmeal.survey.repository.SurveyRepository;
import com.example.thehealingmeal.survey.repository.SurveyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuGenerater {

    private final UserRepository userRepository;

    //식단 저장할 repositories
    private final MenuRepository menuRepository;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final SnackOrTeaMenuRepository snackOrTeaMenuRepository;

    //유저의 열탄단지 결과를 가져올 repository
    private final SurveyResultRepository surveyResultRepository;

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
    public MenuResponseDto generateMenu(Meals meals, Long user_id){
        //식품 테이블에서 대표메뉴, 반찬, 밥을 랜덤하게 각각 가져옴. 단, 필터링을 적용함.
        //아래 코드는 난수를 생성하여 랜덤하게 식단을 가져오게 할 class

        /*
            대표메뉴
        */
        long recordCountForMain = mainDishCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수.

        Survey survey = surveyRepository.findByUserId(user_id); //유저의 설문조사 번호를 찾기 위한 변수
        FilterFood userFilter = filterFoodRepository.findFilterFoodBySurveyId(survey.getId()); //유저의 필터링 내용 가져오기

        //유저필터링 내용 저장
        //dto로 가져온 필터 키워드는 접미사로 -Keyword인 리스트에만 저장하고, ','를 제외한 List로 옮길 땐 접미사가 -List임.
        String[] userKeywords = {userFilter.getStewsAndHotpots(),userFilter.getGrilledFood(),userFilter.getGrilledFood(),userFilter.getPancakeFood()};

        //필터링 리스트를 하나로 배열.
        List<String> filterList = Arrays.stream(userKeywords)
                .flatMap(food -> Arrays.stream(food.split(",")))
                .toList();

        //랜덤하게 대표메뉴 가져오고, null이 아닌지 재차 확인함.
        Optional<MainDishCategory> optionalMainDish = mainDishCategoryRepository.findById(secureRandom.nextLong(recordCountForMain+1));
        while (optionalMainDish.isEmpty()){
            optionalMainDish = mainDishCategoryRepository.findById(secureRandom.nextLong(recordCountForMain+1));
        }

        //대표식품명이 필터링 키워드에 포함되지 않을 때까지 반복한다.
        while(filterList.contains(optionalMainDish.get().getRepresentativeFoodName())){
            optionalMainDish = mainDishCategoryRepository.findById(secureRandom.nextLong(recordCountForMain+1));
            if (optionalMainDish.isEmpty()){
                optionalMainDish = mainDishCategoryRepository.findById(secureRandom.nextLong(recordCountForMain+1));
            }
        }
        MainDishCategory mainDishCategory = optionalMainDish.get();


        /*
            밥
         */
        long recordCountForRice = riceCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수

        Optional<RiceCategory> riceCategoryOptional = riceCategoryRepository.findById(secureRandom.nextLong(recordCountForRice+1));
        while(riceCategoryOptional.isEmpty()) { //rice가 null일 경우를 대비함.
            riceCategoryOptional = riceCategoryRepository.findById(secureRandom.nextLong(recordCountForRice+1));
        }
        RiceCategory riceCategory = riceCategoryOptional.get();
        /*
            반찬 2~3개
         */
        long recordCountForSide = sideDishCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수
        int randomSideNumber = secureRandom.nextInt(2,4); //2~3개 중 몇 개의 반찬을 뽑을 것인지 정하는 변수
        int kcalSide = 0;
        float proteinSide = 0;
        float carbohydrateSide = 0;
        float fatSide = 0;

        //향후 결과값으로 반환할 땐 SideDishesDto 클래스로 할 것.
        //랜덤으로 정해진 반찬의 갯수만큼 반복
        List<SideDishCategory> sideDishCategories = new ArrayList<>();
        for (int start = 0; start < randomSideNumber; start++){
            Optional<SideDishCategory> sideDishCategory = sideDishCategoryRepository.findById(secureRandom.nextLong(recordCountForSide));
            while(sideDishCategory.isEmpty()){
                sideDishCategory = sideDishCategoryRepository.findById(secureRandom.nextLong(recordCountForSide));
            }
            SideDishCategory side = sideDishCategory.get();
            sideDishCategories.add(side);
            kcalSide += sideDishCategories.get(start).getKcal();
            proteinSide += sideDishCategories.get(start).getProtein();
            carbohydrateSide += sideDishCategories.get(start).getCarbohydrate();
            fatSide += sideDishCategories.get(start).getFat();
        }
        //SideDishCateGory 객체로 리스트를 만들고 여기서 따로 반찬명 문자열 리스트 만들어서 필터 및 중복 검사를 하는데 편리하다고 생각하기 때문에 생성
        List<String> sideDishNames = new ArrayList<>();
        for (SideDishCategory name : sideDishCategories){
            sideDishNames.add(name.getRepresentativeFoodName());
        }

        //필터링 : 김치류, 볶음류, 나물 숙채류, 생채 무침류, 수조어육류, 장아찌 절임류, 젓갈류, 조림류
        String[] sideDishFilterKeywords = {userFilter.getVegetableFood(),userFilter.getStirFriedFood(),userFilter.getStewedFood()};
        List<String> sideDishFilterList = new ArrayList<>();
        for (String food : sideDishFilterKeywords) {
            sideDishFilterList.addAll(Arrays.asList(food.split(",")));
        }

        //필터 및 중복 검사
        for(int start = 0; start < sideDishCategories.size(); start++){
            //필터링
            while (sideDishFilterList.contains(sideDishNames.get(start))){
                //필터링 키워드에 가져온 식단명이 일치한다면(필터링에 저촉되면) 이에 포함되지 않는 새로운 식품을 찾아서 반환함.
                Optional<SideDishCategory> optional = sideDishCategoryRepository.findById(secureRandom.nextLong(recordCountForSide));
                while(optional.isEmpty()){
                    optional = sideDishCategoryRepository.findById(secureRandom.nextLong(recordCountForSide));
                }
                SideDishCategory side = optional.get();
                sideDishCategories.set(start, side);
                //반찬의 음식명만을 모은 리스트도 이와 같이 갱신함.
                sideDishNames.set(start, sideDishCategories.get(start).getRepresentativeFoodName());
            }
            //중복 검사
            if (start>0){
                while (sideDishNames.contains(sideDishCategories.get(start).getRepresentativeFoodName())){
                    Optional<SideDishCategory> optional = sideDishCategoryRepository.findById(secureRandom.nextLong(recordCountForSide));
                    while(optional.isEmpty()){
                        optional = sideDishCategoryRepository.findById(secureRandom.nextLong(recordCountForSide));
                    }
                    SideDishCategory side = optional.get();
                    sideDishCategories.set(start, side);
                }
                sideDishNames.set(start, sideDishCategories.get(start).getRepresentativeFoodName());
            }
        }

        /*
            열탄단지 합산
         */
        int kcal = mainDishCategory.getKcal() + riceCategory.getKcal() + kcalSide;
        float protein = mainDishCategory.getProtein() + riceCategory.getProtein() + proteinSide ;
        float carbohydrate = mainDishCategory.getCarbohydrate() + riceCategory.getCarbohydrate() + carbohydrateSide;
        float fat = mainDishCategory.getFat() + riceCategory.getFat() + fatSide;

        Optional<User> optionalUser = userRepository.findById(user_id);
        User user = optionalUser.get();

        //식단 반환
        return MenuResponseDto.builder()
                .main_dish(mainDishCategory.getRepresentativeFoodName())
                .imageURL("https://storage.googleapis.com/"+bucket_name+"/"+mainDishCategory.getRepresentativeFoodName()+".jpg")
                .sideDishForUserMenu(sideDishNames)
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

        //filterFood에서 가져온 필터링 키워드
        String[] snackOrTeaFilterKeywords = {userFilter.getBreadAndConfectionery(), userFilter.getDairyProducts(), userFilter.getBeveragesAndTeas()};

        //콤마를 기준으로 필터링 키워드 리스트 작성
        List<String> filterList = new ArrayList<>();
        for (String filter : snackOrTeaFilterKeywords){
            filterList.addAll(Arrays.asList(filter.split(",")));
        }

        Optional<SnackOrTeaCategory> optional = snackOrTeaCategoryRepository.findById(secureRandom.nextLong(recordCountForSnackOrTea+1));
        SnackOrTeaCategory snackOrTeaCategory;
        while (filterList.contains(optional.get().getRepresentativeFoodName()) || optional.isEmpty()) {
            optional = snackOrTeaCategoryRepository.findById(secureRandom.nextLong(recordCountForSnackOrTea + 1));
        }
        snackOrTeaCategory = optional.get();

        SnackURL snackURL = snackUrlRepository.findBySnackUrlNameStartingWith(snackOrTeaCategory.getRepresentativeFoodName());

        Optional<User> optionalUser = userRepository.findById(user_id);
        User user = optionalUser.get();

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


    //생성한 식단의 유효성을 검사하는 메소드
    public boolean isExceed(Long user_id, MenuResponseDto morning,
                              SnackOrTeaResponseDto morningSnackOrTea,
                              MenuResponseDto lunch,
                              SnackOrTeaResponseDto lunchSnackOrTea,
                              MenuResponseDto dinner){
        //유저의 설문조사 결과치를 가져옴. 이것은 식단 재생성에 대한 기준이 될 것. 해당 유저의 결과치를 초과하지 않도록 함.
        SurveyResult surveyResult = surveyResultRepository.findByUserId(user_id);
        int kcal_result = surveyResult.getKcal();
        float protein_result = surveyResult.getProtein();
        float fat_result = surveyResult.getFat();
        float carborhydrate_result = surveyResult.getCarbohydrate();

        int total_kcal = morning.getKcal() +
                morningSnackOrTea.getKcal() +
                lunch.getKcal() +
                lunchSnackOrTea.getKcal() +
                dinner.getKcal();
        float total_protein = morning.getProtein() +
                morningSnackOrTea.getProtein() +
                lunch.getProtein() +
                lunchSnackOrTea.getProtein() +
                dinner.getProtein();
        float total_fat = morning.getFat() +
                morningSnackOrTea.getFat() +
                lunch.getFat() +
                lunchSnackOrTea.getFat() +
                dinner.getFat();
        float total_carborhydrate = morning.getCarbohydrate() +
                morningSnackOrTea.getCarbohydrate() +
                lunch.getCarbohydrate() +
                lunchSnackOrTea.getCarbohydrate() +
                dinner.getCarbohydrate();
        
        //식단의 열탄단지 합이 설문조사한 유저의 하루필요량을 하나라도 넘으면 true 아니면, false 반환
        return total_kcal > kcal_result || total_carborhydrate > carborhydrate_result || total_fat > fat_result || total_protein > protein_result;
    }
}
