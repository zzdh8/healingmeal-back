package com.example.thehealingmeal.menu.service;

import com.example.thehealingmeal.gpt.responseRepository.ResponseRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.MenuForUser;
import com.example.thehealingmeal.menu.domain.Nutrient;
import com.example.thehealingmeal.menu.domain.repository.MenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SideDishForUserMenuRepository;
import com.example.thehealingmeal.menu.domain.repository.SnackOrTeaMenuRepository;
import com.example.thehealingmeal.survey.domain.SurveyResult;
import com.example.thehealingmeal.survey.repository.SurveyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MenuManager {
    private final SurveyResultRepository surveyResultRepository;
    private final MenuRepository menuRepository;
    private final SideDishForUserMenuRepository sideDishForUserMenuRepository;
    private final SnackOrTeaMenuRepository snackOrTeaMenuRepository;
    private final ResponseRepository responseRepository;

    //생성된 식단의 유효성을 검사하는 메소드
    public boolean isExceed(Long user_id, MenuResponseDto morning,
                            SnackOrTeaResponseDto morningSnackOrTea,
                            MenuResponseDto lunch,
                            SnackOrTeaResponseDto lunchSnackOrTea,
                            MenuResponseDto dinner){
        //유저의 설문조사 결과치를 가져옴. 이 결과치를 초과하지 않도록 함.
        SurveyResult surveyResult = surveyResultRepository.findByUserId(user_id);
        Nutrient totalNutrients = morning.getTotalNutrients()
                .add(morningSnackOrTea.getTotalNutrients())
                .add(lunch.getTotalNutrients())
                .add(lunchSnackOrTea.getTotalNutrients())
                .add(dinner.getTotalNutrients());
        //식단의 열탄단지 합이 설문조사한 유저의 하루필요량을 하나라도 넘으면 true 아니면, false 반환
        return surveyResult.exceeds(totalNutrients);
    }


    //오전 00시 유저 식단 초기화 메소드
    @Transactional
    public void resetMenu(long user_id) throws RuntimeException{
        List<MenuForUser> menu = menuRepository.findAllByUserId(user_id);
        for (MenuForUser m : menu) {
            sideDishForUserMenuRepository.deleteAllByMenuForUser_Id(m.getId());

        }
        snackOrTeaMenuRepository.deleteAllByUserId(user_id);
        menuRepository.deleteAllByUserId(user_id);
        responseRepository.deleteAllByUserId(user_id);
    }

    //식단 확인
    public boolean checkMenu(long user_id) throws NoSuchElementException {
        try {
            return menuRepository.existsByUserId(user_id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Such Element Error : please check user_id");
        }
    }
}
