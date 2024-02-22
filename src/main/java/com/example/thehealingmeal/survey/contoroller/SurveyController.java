package com.example.thehealingmeal.survey.contoroller;



import com.example.thehealingmeal.survey.domain.FilterFood;
import com.example.thehealingmeal.survey.domain.Survey;
import com.example.thehealingmeal.survey.dto.FilterFoodRequestDto;
import com.example.thehealingmeal.survey.dto.SurveyRequestDto;
import com.example.thehealingmeal.survey.dto.SurveyResultDto;
import com.example.thehealingmeal.survey.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {
    private final SurveyService surveyService;


    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }
    // 설문 저장.
    @PostMapping("/{userId}/survey")
    public ResponseEntity<Long> saveServey(@RequestBody SurveyRequestDto surveyRequestDto, @PathVariable Long userId) {
        Survey survey = surveyService.submitSurvey(surveyRequestDto, userId);
        return new ResponseEntity<>(survey.getUser().getId(), HttpStatus.OK); // 해당 값을 프론트에서 보내서 filterFood 저장할 때 url로 매핑.
    }

    // 음식 필터 설문 저장.
    @PostMapping("/{surveyId}/filterFood")
    public ResponseEntity<Long> saveFilter(@RequestBody FilterFoodRequestDto filterFoodRequestDto, @PathVariable Long surveyId) {
       FilterFood filterFood = surveyService.submitFilterFood(filterFoodRequestDto, surveyId);
        return new ResponseEntity<>(filterFood.getId(), HttpStatus.OK);
    }
    // 설문 결과 조회
    @GetMapping("/{userId}/surveyResult")
    public ResponseEntity<SurveyResultDto> surveyResult(@PathVariable Long userId){
        SurveyResultDto surveyResultDto = surveyService.surveyResult(userId);
        return new ResponseEntity<>(surveyResultDto, HttpStatus.OK);
    }

    // 설문 조사 확인
    @GetMapping("/{userId}/checkingSurvey")
    public ResponseEntity<String> checkingSurvey(@PathVariable Long userId){
        if (surveyService.checkingSurvey(userId)){
            return new ResponseEntity<>("설문 내용 있음.",HttpStatus.OK);
        }
        return new ResponseEntity<>("설문 내용 없음.",HttpStatus.OK);
    }

    // 설문 조사 수정
    @PutMapping("/{userId}/change/survey")
    public ResponseEntity<String> changeSurvey(@PathVariable Long userId,@RequestBody SurveyRequestDto surveyRequestDto) {
       surveyService.surveyUpdateByUserId(userId, surveyRequestDto);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }

    // 음식 필터 수정
    @PutMapping("/{surveyId}/change/filterFood")
    public ResponseEntity<String> changeFillterFood(@PathVariable Long surveyId,@RequestBody FilterFoodRequestDto filterFoodRequestDto) {
        surveyService.filterFoodUpdateBySurveyId(surveyId,filterFoodRequestDto);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }
}
