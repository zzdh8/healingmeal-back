package com.example.thehealingmeal.ai;

import io.github.flashvayne.chatgpt.dto.ChatResponse;
import io.github.flashvayne.chatgpt.service.impl.DefaultChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Aicontroller {
    private final CustomChatGPTService customChatGPTService;

    @PostMapping("/ai")
    public ResponseEntity<List<String>> getAnswer() {
        return new ResponseEntity<>(List.of(customChatGPTService.getAnswer()), HttpStatus.OK);
    }

}
