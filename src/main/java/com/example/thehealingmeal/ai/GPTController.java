package com.example.thehealingmeal.ai;

import com.example.thehealingmeal.ai.dto.AiResDto;
import com.example.thehealingmeal.menu.domain.Meals;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GPTController {

    private final CustomChatGPTService customChatGPTService;

    //식단별 효능 정보 생성
    @PostMapping("/{id}/ai/generate")
    public ResponseEntity<String> getAnswerMainDish(@PathVariable long id){
        try {
            AiResDto answer = customChatGPTService.getAnswer(id, Meals.BREAKFAST);
            AiResDto answer1 = customChatGPTService.getAnswer(id, Meals.LUNCH);
            AiResDto answer2 = customChatGPTService.getAnswer(id, Meals.DINNER);
            AiResDto answer3 = customChatGPTService.getAnswerSnackOrTea(id, Meals.BREAKFAST_SNACKORTEA);
            AiResDto answer4 = customChatGPTService.getAnswerSnackOrTea(id, Meals.LUNCH_SNACKORTEA);
            customChatGPTService.saveResponse(answer.getAnswer(), id, Meals.BREAKFAST);
            customChatGPTService.saveResponse(answer1.getAnswer(), id, Meals.LUNCH);
            customChatGPTService.saveResponse(answer2.getAnswer(), id, Meals.DINNER);
            customChatGPTService.saveResponse(answer3.getAnswer(), id, Meals.BREAKFAST_SNACKORTEA);
            customChatGPTService.saveResponse(answer4.getAnswer(), id, Meals.LUNCH_SNACKORTEA);
            return new ResponseEntity<>("Request Success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Request Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/breakfast")
    public ResponseEntity<AiResDto> getBreakfast(@PathVariable long id){
        try {
            return new ResponseEntity<>(customChatGPTService.provideResponse(id, Meals.BREAKFAST), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/lunch")
    public ResponseEntity<AiResDto> getLunch(@PathVariable long id){
        try {
            return new ResponseEntity<>(customChatGPTService.provideResponse(id, Meals.LUNCH), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/dinner")
    public ResponseEntity<AiResDto> getDinner(@PathVariable long id){
        try {
            return new ResponseEntity<>(customChatGPTService.provideResponse(id, Meals.DINNER), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/breakfast-snackortea")
    public ResponseEntity<AiResDto> getBreakfastSnackOrTea(@PathVariable long id){
        try {
            return new ResponseEntity<>(customChatGPTService.provideResponse(id, Meals.BREAKFAST_SNACKORTEA), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/lunch-snackortea")
    public ResponseEntity<AiResDto> getLunchSnackOrTea(@PathVariable long id){
        try {
            return new ResponseEntity<>(customChatGPTService.provideResponse(id, Meals.LUNCH_SNACKORTEA), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
