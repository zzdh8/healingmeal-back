package com.example.thehealingmeal.survey.repository;


import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.survey.doamin.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {
    SurveyResult findSurveyResultByUser(User user);

    SurveyResult findByUserId(Long userId);
}
