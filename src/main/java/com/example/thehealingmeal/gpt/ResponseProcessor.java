package com.example.thehealingmeal.gpt;

import com.example.thehealingmeal.gpt.dto.AiResDto;
import com.example.thehealingmeal.menu.domain.Meals;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResponseProcessor {
    private final GPTService gptService;

    //gpt response processing -> save to db
    @Transactional
    public void processResponse(long user_id) {
        for (Meals meal : Meals.values()) {
            AiResDto answer;
            if (meal == Meals.BREAKFAST_SNACKORTEA || meal == Meals.LUNCH_SNACKORTEA) {
                answer = gptService.getAnswerSnackOrTea(user_id, meal);
            } else {
                answer = gptService.getAnswer(user_id, meal);
            }
            gptService.saveResponse(answer.getAnswer(), user_id, meal);
        }
    }
}
