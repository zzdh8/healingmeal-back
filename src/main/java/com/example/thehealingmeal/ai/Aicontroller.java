package com.example.thehealingmeal.ai;

import com.example.thehealingmeal.ai.dto.AiResDto;
import com.example.thehealingmeal.menu.domain.Meals;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Aicontroller {

    private final CustomChatGPTService customChatGPTService;

    //아침
    @GetMapping("/{id}/ai/breakfast")
    public ResponseEntity<AiResDto> getAnswerBreakfast(@PathVariable long id){
        return new ResponseEntity<>(customChatGPTService.getAnswer(id, Meals.BREAKFAST), HttpStatus.OK);
    }

    //점심
    @GetMapping("/{id}/ai/lunch")
    public ResponseEntity<AiResDto> getAnswerLunch(@PathVariable long id){
        return new ResponseEntity<>(customChatGPTService.getAnswer(id, Meals.LUNCH), HttpStatus.OK);
    }

    //저녁
    @GetMapping("/{id}/ai/dinner")
    public ResponseEntity<AiResDto> getAnswerDinner(@PathVariable long id){
        return new ResponseEntity<>(customChatGPTService.getAnswer(id, Meals.DINNER), HttpStatus.OK);
    }

    //아침-점심 간식
    @GetMapping("/{id}/ai/breakfast-snack-or-tea")
    public ResponseEntity<AiResDto> getAnswerBreakfastSnackOrTea(@PathVariable long id){
        return new ResponseEntity<>(customChatGPTService.getAnswerSnackOrTea(id, Meals.BREAKFAST_SNACKORTEA), HttpStatus.OK);
    }

    //점심-저녁 간식
    @GetMapping("/{id}/ai/lunch-snack-or-tea")
    public ResponseEntity<AiResDto> getAnswerLunchSnackOrTea(@PathVariable long id){
        return new ResponseEntity<>(customChatGPTService.getAnswerSnackOrTea(id, Meals.LUNCH_SNACKORTEA), HttpStatus.OK);
    }
}
