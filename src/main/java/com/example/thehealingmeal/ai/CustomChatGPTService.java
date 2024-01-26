package com.example.thehealingmeal.ai;

import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomChatGPTService {
    private final ChatgptService chatgptService;

   public String getAnswer() {
       return chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user","how are you?")));
    }
}
