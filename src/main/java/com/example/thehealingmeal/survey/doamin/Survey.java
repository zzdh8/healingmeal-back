package com.example.thehealingmeal.survey.doamin;


import com.example.thehealingmeal.member.domain.Gender;
import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.survey.dto.SurveyRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    private Long age;

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

    @OneToOne(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FilterFood filterFood;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 정적 팩토리 메서드
    public static Survey createSurvey(SurveyRequestDto surveyRequestDto, User user) {
        return Survey.builder()
                .age(surveyRequestDto.getAge())
                .diabetesType(surveyRequestDto.getDiabetesType())
                .numberOfExercises(surveyRequestDto.getNumberOfExercises())
                .height(surveyRequestDto.getHeight())
                .weight(surveyRequestDto.getWeight())
                .gender(surveyRequestDto.getGender())
                .standardWeight(surveyRequestDto.getStandardWeight())
                .bodyMassIndex(surveyRequestDto.getBodyMassIndex())
                .caloriesNeededPerDay(surveyRequestDto.getCaloriesNeededPerDay())
                .weightLevel(surveyRequestDto.getWeightLevel())
                .user(user)
                .build();
    }

    public void update(SurveyRequestDto surveyRequestDto) {
        this.age = surveyRequestDto.getAge();
        this.diabetesType = surveyRequestDto.getDiabetesType();
        this.numberOfExercises = surveyRequestDto.getNumberOfExercises();
        this.height = surveyRequestDto.getHeight();
        this.weight = surveyRequestDto.getWeight();
        this.gender = surveyRequestDto.getGender();
        this.standardWeight = surveyRequestDto.getStandardWeight();
        this.bodyMassIndex = surveyRequestDto.getBodyMassIndex();
        this.caloriesNeededPerDay = surveyRequestDto.getCaloriesNeededPerDay();
        this.weightLevel = surveyRequestDto.getWeightLevel();

    }

}
