package com.example.thehealingmeal.menu.service.recommend;


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
import com.example.thehealingmeal.menu.domain.ExceedInfo;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.domain.SnackURL;
import com.example.thehealingmeal.menu.domain.repository.SnackUrlRepository;
import com.example.thehealingmeal.survey.domain.FilterFood;
import com.example.thehealingmeal.survey.repository.FilterFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@EnableCaching
public class MenuGenerater {
    private final UserRepository userRepository;

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
    @Cacheable(value = "randomNumbers")
    public List<Long> generateRandomNumbers(long max) {
        List<Long> numbers = LongStream.rangeClosed(1, max).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
        return numbers.subList(0, numbers.size());
    }

    //랜덤값을 활용한 메뉴 추출 메소드
    @Async("threadPoolTaskExecutor")
    protected <T> CompletableFuture<T> getRandomMenu(JpaRepository<T, Long> repository, List<Long> randomIds, Predicate<T> filter) {
        Optional<T> optionalItem;
        int count = 0;
        int startPoint = secureRandom.nextInt(0, 2);
        do {
            if (startPoint == 0) {
                count = (randomIds.size() - 1) / secureRandom.nextInt(2, randomIds.size() - 2);
                optionalItem = repository.findById(randomIds.get(count++));
            } else {
                count = randomIds.size() - secureRandom.nextInt(randomIds.size() / 3, randomIds.size() / 2);
                optionalItem = repository.findById(randomIds.get(count--));
            }
        } while (optionalItem.isEmpty() || filter.test(optionalItem.get()));
        return CompletableFuture.completedFuture(optionalItem.get());
    }

    @Async("threadPoolTaskExecutor")
    protected <T> CompletableFuture<T> getRandomSide(JpaRepository<T, Long> repository, List<Long> randomIds, Predicate<T> filter, List<SideDishCategory> sideDishCategories) {
        Optional<T> optionalItem;
        int count = 0;
        int startPoint = secureRandom.nextInt(0, 3);
        do {
            if (startPoint == 0) {
                optionalItem = repository.findById(randomIds.get(count++));
            } else if (startPoint == 1) {
                count = randomIds.size() - secureRandom.nextInt(randomIds.size() / 3, randomIds.size() / 2);
                optionalItem = repository.findById(randomIds.get(count--));
            } else {
                count = (randomIds.size() - 1) / secureRandom.nextInt(2, randomIds.size() - 2);
                optionalItem = repository.findById(randomIds.get(count++));
            }
            if (!sideDishCategories.isEmpty()) {
                for (SideDishCategory sideDishCategory : sideDishCategories) {
                    if (sideDishCategory.getRepresentativeFoodName().equals(((SideDishCategory) optionalItem.get()).getRepresentativeFoodName())) {
                        optionalItem = repository.findById(randomIds.get(secureRandom.nextInt(0, randomIds.size() - 1)));
                    }
                }
            }
        } while (optionalItem.isEmpty() || filter.test(optionalItem.get()));
        return CompletableFuture.completedFuture(optionalItem.get());
    }

    //식단 생성 메소드
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<MenuResponseDto> generateMenu(Meals meals, Long user_id, List<Long> main, List<Long> side, List<Long> rice) throws NoSuchElementException, IllegalArgumentException, NullPointerException, ExecutionException, InterruptedException {

        //대표메뉴 Main Dish
        //유저필터링 키워드를 콤마를 기준으로 리스트로 만들어줌.
        List<String> filterList = getMainDishFilter(user_id);
        MainDishCategory mainDishCategory = getRandomMenu(mainDishCategoryRepository,
                main,
                item -> filterList.contains(item.getRepresentativeFoodName())).get();

        // 밥 Rice
        Optional<RiceCategory> rice1 = riceCategoryRepository.findById(secureRandom.nextInt(1, rice.size() + 1));
        RiceCategory riceCategory = rice1.get();

        //반찬 2~3개 SideDishes random 2~3
        List<String> sideDishFilterList = getSideDishFilter(user_id);
        List<SideDishCategory> sideDishCategories = new ArrayList<>(); //반찬 리스트
        for (int start = 0; start < secureRandom.nextInt(2, 4); start++) {
            sideDishCategories.add(getRandomSide(sideDishCategoryRepository, side, item -> sideDishFilterList.contains(item.getRepresentativeFoodName()), sideDishCategories).get());
        }
        //열탄단지 합산 Kcal, Protein, Carbohydrate, Fat adding
        int kcal = kcalSum(mainDishCategory.getKcal(), riceCategory.getKcal(), sideDishCategories); //열량 합산 (kcal
        float protein = proteinSum(mainDishCategory.getProtein(), riceCategory.getProtein(), sideDishCategories); //단백질 합산 (g)
        float carbohydrate = carbohydrateSum(mainDishCategory.getCarbohydrate(), riceCategory.getCarbohydrate(), sideDishCategories); //탄수화물 합산 (g)
        float fat = fatSum(mainDishCategory.getFat(), riceCategory.getFat(), sideDishCategories); //지방 합산 (g)

        User user = userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        MenuResponseDto menuResponseDto = MenuResponseDto.builder()
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
        return CompletableFuture.completedFuture(menuResponseDto);
    }

    //간식 생성
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<SnackOrTeaResponseDto> generateSnackOrTea(Meals meals, long user_id, List<Long> snack) throws ExecutionException, InterruptedException {

        //generate snack or tea filtering keyword
        List<String> filterList = getMainDishFilter(user_id);

        SnackOrTeaCategory snackOrTeaCategory = getRandomMenu(snackOrTeaCategoryRepository,
                snack,
                item -> filterList.contains(item.getRepresentativeFoodName())).get();

        SnackURL snackURL = snackUrlRepository.findBySnackUrlNameStartingWith(snackOrTeaCategory.getRepresentativeFoodName());

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        SnackOrTeaResponseDto snackOrTeaResponseDto = SnackOrTeaResponseDto.builder()
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
        return CompletableFuture.completedFuture(snackOrTeaResponseDto);
    }

    int kcalSum(int kcal1, int kcal2, List<SideDishCategory> sideDishCategories) {
        return kcal1 + kcal2 + sideDishCategories.stream().mapToInt(SideDishCategory::getKcal).sum();
    }

    float proteinSum(float protein1, float protein2, List<SideDishCategory> sideDishCategories) {
        return protein1 + protein2 + (float) sideDishCategories.stream().mapToDouble(SideDishCategory::getProtein).sum();
    }

    float carbohydrateSum(float carbohydrate1, float carbohydrate2, List<SideDishCategory> sideDishCategories) {
        return carbohydrate1 + carbohydrate2 + (float) sideDishCategories.stream().mapToDouble(SideDishCategory::getCarbohydrate).sum();
    }

    float fatSum(float fat1, float fat2, List<SideDishCategory> sideDishCategories) {
        return fat1 + fat2 + (float) sideDishCategories.stream().mapToDouble(SideDishCategory::getFat).sum();
    }

    //return filterFood MainDish
    List<String> getMainDishFilter(long user_id) {
        FilterFood userFilter = filterFoodRepository.findFilterFoodByUserId(user_id); //유저의 필터링 내용 가져오기
        if (userFilter == null) {
            userFilter = FilterFood.builder()
                    .stewsAndHotpots("")
                    .grilledFood("")
                    .pancakeFood("")
                    .vegetableFood("")
                    .stirFriedFood("")
                    .stewedFood("")
                    .beveragesAndTeas("")
                    .breadAndConfectionery("")
                    .dairyProducts("")
                    .build();
        }
        return Arrays.asList((userFilter.getStewsAndHotpots() + "," + userFilter.getGrilledFood() + "," + userFilter.getPancakeFood()).split(","));
    }

    //return filterFood Side Dishes
    List<String> getSideDishFilter(long user_id) {
        FilterFood userFilter = filterFoodRepository.findFilterFoodByUserId(user_id); //유저의 필터링 내용 가져오기
        if (userFilter == null) {
            userFilter = FilterFood.builder()
                    .stewsAndHotpots("")
                    .grilledFood("")
                    .pancakeFood("")
                    .vegetableFood("")
                    .stirFriedFood("")
                    .stewedFood("")
                    .beveragesAndTeas("")
                    .breadAndConfectionery("")
                    .dairyProducts("")
                    .build();
        }
        return Arrays.asList((userFilter.getVegetableFood() + "," + userFilter.getStirFriedFood() + "," + userFilter.getStewedFood()).split(",")); //필터링 키워드
    }

    /*
    식단 다시 불러오기 메소드
     */
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<MenuResponseDto> reloadMenu(Long user_id, MenuResponseDto menu, ExceedInfo exceedInfo) {
        List<MainDishCategory> mains;
        List<SideDishCategory> sideDishCategories = new ArrayList<>();
        List<SideDishCategory> sides;
        MainDishCategory mainDishCategory;

        if (exceedInfo.getProteinExceed() >= 50) {
            mains = mainDishCategoryRepository.findMainDishCategoriesByProteinLessThanOrderByProteinAsc(menu.getProtein() - exceedInfo.getProteinExceed());
            if (mains.isEmpty()) {
                mainDishCategory = mainDishCategoryRepository.findMainDishCategoryByRepresentativeFoodName(menu.getMain_dish()).orElseThrow(() -> new IllegalArgumentException("Main Dish not found."));
            } else {
                mainDishCategory = mains.get(0);
            }
            sides = sideDishCategoryRepository.findSideDishCategoryByProteinLessThanOrderByProteinAsc(menu.getProtein() - exceedInfo.getProteinExceed());
            if (sides.isEmpty()) {
                for (int start = 0; start < 2; start++) {
                    sideDishCategories.add(sideDishCategoryRepository.findSideDishCategoryByRepresentativeFoodName(menu.getSideDishForUserMenu().get(start)).orElseThrow(() -> new IllegalArgumentException("Side Dish not found.")));
                }
            } else {
                for (int start = 0; start < 2; start++) {
                    sideDishCategories.add(sides.get(0));
                }
            }
        } else {
            mains = mainDishCategoryRepository.findMainDishCategoriesByKcalLessThanOrCarbohydrateLessThanOrProteinLessThanOrFatLessThanOrderByKcalAsc(
                    menu.getKcal() - exceedInfo.getKcalExceed(),
                    menu.getCarbohydrate() - exceedInfo.getCarbohydrateExceed(),
                    menu.getProtein() - exceedInfo.getProteinExceed(),
                    menu.getFat() - exceedInfo.getFatExceed());
            mainDishCategory = mains.get(secureRandom.nextInt(0, mains.size() - 1));

            sides = sideDishCategoryRepository.findSideDishCategoryByKcalLessThanOrCarbohydrateLessThanOrProteinLessThanOrFatLessThanOrderByKcalAsc(
                    menu.getKcal() - exceedInfo.getKcalExceed(),
                    menu.getCarbohydrate() - exceedInfo.getCarbohydrateExceed(),
                    menu.getProtein() - exceedInfo.getProteinExceed(),
                    menu.getFat() - exceedInfo.getFatExceed());
            for (int start = 0; start < 2; start++) {
                sideDishCategories.add(sides.get(secureRandom.nextInt(0, sides.size() - 1)));
            }
        }


        RiceCategory riceCategory = riceCategoryRepository.findByRepresentativeFoodName(menu.getRice());
        User user = userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("User not found."));

        int kcal = kcalSum(mainDishCategory.getKcal(), riceCategory.getKcal(), sideDishCategories);
        float protein = proteinSum(mainDishCategory.getProtein(), riceCategory.getProtein(), sideDishCategories);
        float carbohydrate = carbohydrateSum(mainDishCategory.getCarbohydrate(), riceCategory.getCarbohydrate(), sideDishCategories);
        float fat = fatSum(mainDishCategory.getFat(), riceCategory.getFat(), sideDishCategories);


        MenuResponseDto menuResponseDto = MenuResponseDto.builder()
                .main_dish(mainDishCategory.getRepresentativeFoodName())
                .imageURL("https://storage.googleapis.com/" + bucket_name + "/" + mainDishCategory.getRepresentativeFoodName() + ".jpg")
                .sideDishForUserMenu(sideDishCategories.stream().map(SideDishCategory::getRepresentativeFoodName).collect(Collectors.toList()))
                .rice(menu.getRice())
                .kcal(kcal)
                .protein(protein)
                .carbohydrate(carbohydrate)
                .fat(fat)
                .meals(exceedInfo.getMeals())
                .user_id(user_id)
                .user(user)
                .build();
        return CompletableFuture.completedFuture(menuResponseDto);
    }
}
