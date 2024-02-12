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
import com.example.thehealingmeal.survey.domain.FilterFood;
import com.example.thehealingmeal.survey.domain.Survey;
import com.example.thehealingmeal.survey.repository.FilterFoodRepository;
import com.example.thehealingmeal.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
    private final SecureRandom secureRandom = new SecureRandom();

    //랜덤값 생성 메소드
    private List<Long> generateRandomNumbers(long max, int num) {
        List<Long> numbers = LongStream.rangeClosed(1, max).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
        return numbers.subList(0, num);
    }
    //랜덤값을 활용한 메뉴 추출 메소드
    private <T> T getRandomMenu(JpaRepository<T, Long> repository, List<Long> randomIds, Predicate<T> filter) {
        Optional<T> optionalItem;
        int count = 0;
        do {
            optionalItem = repository.findById(randomIds.get(count++));
        } while (optionalItem.isEmpty() || filter.test(optionalItem.get()));
        return optionalItem.get();
    }
    //식단 생성 메소드
    public MenuResponseDto generateMenu(Meals meals, Long user_id) throws NoSuchElementException, IllegalArgumentException, NullPointerException {

        /*
            대표메뉴 Main Dish
        */
        Survey survey = surveyRepository.findByUserId(user_id); //유저의 설문조사 번호를 찾기 위한 변수
        FilterFood userFilter = filterFoodRepository.findFilterFoodBySurveyId(survey.getId()); //유저의 필터링 내용 가져오기

        //유저필터링 키워드를 콤마를 기준으로 리스트로 만들어줌.
        List<String> filterList = Arrays.asList((userFilter.getStewsAndHotpots() + "," + userFilter.getGrilledFood() + "," + userFilter.getPancakeFood()).split(","));
        MainDishCategory mainDishCategory = getRandomMenu(mainDishCategoryRepository,
                generateRandomNumbers(mainDishCategoryRepository.count(), 20),
                item -> filterList.contains(item.getRepresentativeFoodName()));


        /*
            밥 Rice
         */
        RiceCategory riceCategory = getRandomMenu(riceCategoryRepository, generateRandomNumbers(riceCategoryRepository.count(),5), item -> false);

        /*
            반찬 2~3개 SideDishes random 2~3
         */
        List<String> sideDishFilterList = Arrays.asList((userFilter.getVegetableFood() + "," + userFilter.getStirFriedFood() + "," + userFilter.getStewedFood()).split(",")); //필터링 키워드
        List<SideDishCategory> sideDishCategories = new ArrayList<>(); //반찬 리스트
        List<Long> randomSideDishIds = generateRandomNumbers(sideDishCategoryRepository.count(), 20); //랜덤값 생성
        for (int start = 0; start < secureRandom.nextInt(2,4); start++) {
            sideDishCategories.add(getRandomMenu(sideDishCategoryRepository, randomSideDishIds, item -> sideDishFilterList.contains(item.getRepresentativeFoodName())));
        }

        /*
            열탄단지 합산 Kcal, Protein, Carbohydrate, Fat adding
         */
        int kcal = mainDishCategory.getKcal() + riceCategory.getKcal() + sideDishCategories.stream().mapToInt(SideDishCategory::getKcal).sum();
        float protein = (float) (mainDishCategory.getProtein() + riceCategory.getProtein() + sideDishCategories.stream().mapToDouble(SideDishCategory::getProtein).sum());
        float carbohydrate = (float) (mainDishCategory.getCarbohydrate() + riceCategory.getCarbohydrate() + sideDishCategories.stream().mapToDouble(SideDishCategory::getCarbohydrate).sum());
        float fat = (float) (mainDishCategory.getFat() + riceCategory.getFat() + sideDishCategories.stream().mapToDouble(SideDishCategory::getFat).sum());

        User user = userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        //식단 반환 menu return
        return MenuResponseDto.builder()
                .main_dish(mainDishCategory.getRepresentativeFoodName())
                .imageURL("https://storage.googleapis.com/" + bucket_name + "/" + mainDishCategory.getRepresentativeFoodName() + ".jpg")
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

    //간식 생성 아점-점저 사이만 허용 가능
    public SnackOrTeaResponseDto generateSnackOrTea(Meals meals, long user_id) {
        long recordCountForSnackOrTea = snackOrTeaCategoryRepository.count(); //row 수만큼의 랜덤값을 위한 long 변수.

        Survey survey = surveyRepository.findByUserId(user_id); //유저의 설문조사 번호를 찾기 위한 변수
        FilterFood userFilter = filterFoodRepository.findFilterFoodBySurveyId(survey.getId()); //유저의 필터링 내용 가져오기

        //콤마를 기준으로 필터링 키워드 리스트 작성
        List<String> filterList = Arrays.asList((userFilter.getBreadAndConfectionery() + "," + userFilter.getDairyProducts() + "," + userFilter.getBeveragesAndTeas()).split(","));

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

    //간식 저장
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
}
