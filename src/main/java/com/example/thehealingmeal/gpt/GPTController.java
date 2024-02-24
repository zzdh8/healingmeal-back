package com.example.thehealingmeal.gpt;

import com.example.thehealingmeal.gpt.dto.AiResDto;
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

    private final GPTService gptService;
    private final ResponseProcessor responseProcessor;

    //식단별 효능 정보 생성
    @PostMapping("/{id}/ai/generate")
    public ResponseEntity<AiResDto> getAnswerMainDish(@PathVariable long id){
        try {
            responseProcessor.processResponse(id);
            return new ResponseEntity<>(new AiResDto("Response Processed Successfully"), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new AiResDto("Failed to Process Response"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/ai/breakfast")
    public ResponseEntity<AiResDto> getBreakfast(@PathVariable long id){
        try {
            return new ResponseEntity<>(gptService.provideResponse(id, Meals.BREAKFAST), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/lunch")
    public ResponseEntity<AiResDto> getLunch(@PathVariable long id){
        try {
            return new ResponseEntity<>(gptService.provideResponse(id, Meals.LUNCH), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/dinner")
    public ResponseEntity<AiResDto> getDinner(@PathVariable long id){
        try {
            return new ResponseEntity<>(gptService.provideResponse(id, Meals.DINNER), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/breakfast-snackortea")
    public ResponseEntity<AiResDto> getBreakfastSnackOrTea(@PathVariable long id){
        try {
            return new ResponseEntity<>(gptService.provideResponse(id, Meals.BREAKFAST_SNACKORTEA), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/ai/lunch-snackortea")
    public ResponseEntity<AiResDto> getLunchSnackOrTea(@PathVariable long id){
        try {
            return new ResponseEntity<>(gptService.provideResponse(id, Meals.LUNCH_SNACKORTEA), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
