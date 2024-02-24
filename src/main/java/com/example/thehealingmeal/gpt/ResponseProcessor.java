package com.example.thehealingmeal.gpt;

import com.example.thehealingmeal.gpt.dto.AiResDto;
import com.example.thehealingmeal.menu.domain.Meals;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ResponseProcessor {
    private final GPTService gptService;

    //gpt response processing -> save to db
    public void processResponse(long user_id) throws ExecutionException, InterruptedException {
        for (Meals meal : Meals.values()) {
            CompletableFuture<AiResDto> answer;
            if (meal == Meals.BREAKFAST_SNACKORTEA || meal == Meals.LUNCH_SNACKORTEA) {
                answer = gptService.getAnswerSnackOrTea(user_id, meal);
                gptService.saveResponse(answer.get().getAnswer(), user_id, meal);
            } else {
                answer = gptService.getAnswer(user_id, meal);
                gptService.saveResponse(answer.get().getAnswer(), user_id, meal);
            }
        }
    }
}
