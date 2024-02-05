package com.example.thehealingmeal.member.dto;

import com.example.thehealingmeal.member.domain.Gender;
import com.example.thehealingmeal.member.domain.Role;
import com.example.thehealingmeal.member.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalDto {
    // 유저 정보

    private String loginId;

    private String password;

    private String name;

    private String email;

    private String birthDate;

    private String phoneNumber;

    private Role role;

    // 설문 조사 정보
    private String age;

    private Long diabetesType; // 당뇨유형

    private Long numberOfExercises; // 육체 활동 빈도

    private Long height;

    private Long weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Long standardWeight; // 표준 체중

    private Long bodyMassIndex; // 체질량지수

    private Long caloriesNeededPerDay; // 하루 필요 열량

    private String weightLevel; // 현재 체중 단계

    // 필터 내용 정보
    private String stewsAndHotpots; // 찌개와 전골류

    private String stewedFood; // 조림류

    private String stirFriedFood; // 볶음류

    private String grilledFood; // 구이류

    private String vegetableFood; // 나물 숙채류

    private String steamedFood; // 찜류

    private String pancakeFood; // 전 적 및 부침류

    private String breadAndConfectionery; // 빵 및 과자류

    private String beveragesAndTeas; // 음료 및 차류

    private String dairyProducts; // 유제품류

    public static TotalDto fromUser(User user) {
        return TotalDto.builder()
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .age(user.getSurvey().getAge())
                .diabetesType(user.getSurvey().getDiabetesType())
                .numberOfExercises(user.getSurvey().getNumberOfExercises())
                .height(user.getSurvey().getHeight())
                .weight(user.getSurvey().getWeight())
                .gender(user.getSurvey().getGender())
                .standardWeight(user.getSurvey().getStandardWeight())
                .bodyMassIndex(user.getSurvey().getBodyMassIndex())
                .caloriesNeededPerDay(user.getSurvey().getCaloriesNeededPerDay())
                .weightLevel(user.getSurvey().getWeightLevel())
                .stewsAndHotpots(user.getSurvey().getFilterFood().getStewsAndHotpots())
                .stewedFood(user.getSurvey().getFilterFood().getStewedFood())
                .stirFriedFood(user.getSurvey().getFilterFood().getStirFriedFood())
                .grilledFood(user.getSurvey().getFilterFood().getGrilledFood())
                .vegetableFood(user.getSurvey().getFilterFood().getVegetableFood())
                .steamedFood(user.getSurvey().getFilterFood().getSteamedFood())
                .pancakeFood(user.getSurvey().getFilterFood().getPancakeFood())
                .breadAndConfectionery(user.getSurvey().getFilterFood().getBreadAndConfectionery())
                .beveragesAndTeas(user.getSurvey().getFilterFood().getBeveragesAndTeas())
                .dairyProducts(user.getSurvey().getFilterFood().getDairyProducts())
                .build();
    }
}
