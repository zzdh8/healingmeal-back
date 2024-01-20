package com.example.thehealingmeal.survey.service;


import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.survey.doamin.FilterFood;
import com.example.thehealingmeal.survey.doamin.Survey;
import com.example.thehealingmeal.survey.doamin.SurveyResult;
import com.example.thehealingmeal.survey.dto.FilterFoodRequestDto;
import com.example.thehealingmeal.survey.dto.SurveyRequestDto;
import com.example.thehealingmeal.survey.dto.SurveyResultDto;
import com.example.thehealingmeal.survey.repository.FilterFoodRepository;
import com.example.thehealingmeal.survey.repository.SurveyRepository;
import com.example.thehealingmeal.survey.repository.SurveyResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.thehealingmeal.survey.doamin.FilterFood.createFilterFood;
import static com.example.thehealingmeal.survey.doamin.Survey.createSurvey;
import static com.example.thehealingmeal.survey.doamin.SurveyResult.createSurveyResult;


@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final FilterFoodRepository filterFoodRepository;
    private final UserRepository userRepository;
    private final SurveyResultRepository surveyResultRepository;


    public SurveyService(SurveyRepository surveyRepository, FilterFoodRepository filterFoodRepository, UserRepository userRepository, SurveyResultRepository surveyResultRepository) {
        this.surveyRepository = surveyRepository;
        this.filterFoodRepository = filterFoodRepository;
        this.userRepository = userRepository;
        this.surveyResultRepository = surveyResultRepository;
    }

    @Transactional
    public Survey submitSurvey(SurveyRequestDto surveyRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Survey survey = createSurvey(surveyRequestDto, user);

        surveyRepository.save(survey);

        int kcal = Integer.parseInt(survey.getCaloriesNeededPerDay().toString());
        SurveyResult surveyResult = createSurveyResult(
                kcal,
                Float.parseFloat(proteinCalculation(kcal).toString()),
                Float.parseFloat(fatCalculation(kcal).toString()),
                Float.parseFloat(carbohydrateCalculation(kcal).toString()),
                user
        );

        surveyResultRepository.save(surveyResult);

        return survey;
    }
    private Double proteinCalculation(int Kcal) {
        double result = Double.parseDouble(String.valueOf(Kcal)) * 13.5 / 400;
        return Math.round(result * 10) / 10.0;
    }

    private Double fatCalculation(int Kcal) {
        double result = Double.parseDouble(String.valueOf(Kcal)) * 20 / 900;
        return Math.round(result * 10) / 10.0;
    }

    private Double carbohydrateCalculation(int Kcal) {
        double result = Double.parseDouble(String.valueOf(Kcal)) * 62.5 / 400;
        return Math.round(result * 10) / 10.0;
    }

    @Transactional
    public FilterFood submitFilterFood(FilterFoodRequestDto filterFoodRequestDto, Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        FilterFood filterFood = createFilterFood(filterFoodRequestDto, survey);

        filterFoodRepository.save(filterFood);
        return filterFood;
    }
    // 설문 조사 결과
    public SurveyResultDto surveyResult(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        SurveyResult surveyResult = surveyResultRepository.findSurveyResultByUser(user);

        return new SurveyResultDto(
                surveyResult.getKcal(),surveyResult.getProtein(),surveyResult.getCarbohydrate(), surveyResult.getFat());
    }

    public boolean checkingSurvey(Long userId) {
        return surveyRepository.existsSurveyByUserId(userId);
    }

    @Transactional
    public void surveyUpdateByUserId(Long userId, SurveyRequestDto surveyRequestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Survey survey = surveyRepository.findSurveyByUserId(userId);
        survey.update(surveyRequestDto);
        surveyRepository.save(survey);

        int kcal = Integer.parseInt(survey.getCaloriesNeededPerDay().toString());
        SurveyResult surveyResult = createSurveyResult(
                kcal,
                Float.parseFloat(proteinCalculation(kcal).toString()),
                Float.parseFloat(fatCalculation(kcal).toString()),
                Float.parseFloat(carbohydrateCalculation(kcal).toString()),
                user
        );

        // 데이터베이스에서 기존의 surveyResult를 가져옴
        SurveyResult existingSurveyResult = surveyResultRepository.findById(userId).orElseThrow();

        // 기존의 surveyResult를 새로운 값으로 업데이트
        existingSurveyResult.update(surveyResult);
        surveyResultRepository.save(existingSurveyResult);
    }
    @Transactional
    public void filterFoodUpdateBySurveyId(Long surveyId, FilterFoodRequestDto filterFoodRequestDto){
        FilterFood filterFood = filterFoodRepository.findFilterFoodBySurveyId(surveyId);
        filterFood.update(filterFoodRequestDto);
        filterFoodRepository.save(filterFood);
    }
}
