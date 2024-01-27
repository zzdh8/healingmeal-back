package com.example.thehealingmeal.survey.execption;

import com.example.thehealingmeal.survey.dto.SurveyRequestDto;

public class InvalidSurveyException extends RuntimeException {
    public InvalidSurveyException(final String message) {
        super(message);
    }

    public static boolean isInvalid(SurveyRequestDto surveyRequestDto) {
        return surveyRequestDto.getAge() == null
                || surveyRequestDto.getDiabetesType() == null
                || surveyRequestDto.getNumberOfExercises() == null
                || surveyRequestDto.getHeight() == null
                || surveyRequestDto.getWeight() == null
                || surveyRequestDto.getGender() == null
                || surveyRequestDto.getStandardWeight() == null
                || surveyRequestDto.getBodyMassIndex() == null
                || surveyRequestDto.getCaloriesNeededPerDay() == null
                || surveyRequestDto.getWeightLevel() == null;
    }

    public InvalidSurveyException() {
        this("잘못된 설문 내용입니다.");
    }
}
