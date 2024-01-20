package com.example.thehealingmeal.survey.repository;

import com.example.thehealingmeal.survey.doamin.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Long> {
    boolean existsSurveyByUserId(Long userId);
    Survey findSurveyByUserId(Long userId);
    Survey findByUserId(Long userId);
}
