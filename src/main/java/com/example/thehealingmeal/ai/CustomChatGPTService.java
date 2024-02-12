package com.example.thehealingmeal.ai;

import com.example.thehealingmeal.ai.dto.AiResDto;
import com.example.thehealingmeal.ai.responseRepository.GPTResponse;
import com.example.thehealingmeal.ai.responseRepository.ResponseRepository;
import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.service.MenuProvider;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomChatGPTService {
    private final ChatgptService chatgptService;
    private final MenuProvider menuProvider;
    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;

    //GPT Request Main Dish
    public AiResDto getAnswer(long user_id, Meals meals) {
        MenuResponseDto menu = menuProvider.provide(user_id, meals);
        List<String> names = List.of(menu.getMain_dish(), menu.getRice(), menu.getSideDishForUserMenu().toString());
        StringBuilder sentence = new StringBuilder();
        for (String name : names) {
            sentence.append(name);
            if (names.iterator().hasNext()) {
                sentence.append(", ");
            }
        }
        String multiChat = chatgptService.multiChat(List.of(new MultiChatMessage("user", sentence + "을 먹는다고 했을 때 섭취하는 사람에게 어떤 효능이 있을까? 위 아래 문단의 잡설은 하지 말고, 번호 리스트로 짧은 내용만 보여줘.")));
        if (multiChat.isBlank() || multiChat.isEmpty()) {
            multiChat = chatgptService.multiChat(List.of(new MultiChatMessage("user", sentence + "을 먹는다고 했을 때 섭취하는 사람에게 어떤 효능이 있을까? 위 아래 문단의 잡설은 하지 말고, 번호 리스트로 짧은 내용만 보여줘.")));
        }
        return new AiResDto(multiChat);

    }

    //GPT Request Snack or Tea
    public AiResDto getAnswerSnackOrTea(long user_id, Meals meals){
        SnackOrTeaResponseDto menu = menuProvider.provideSnackOrTea(user_id, meals);
        String multiChat = chatgptService.multiChat(List.of(new MultiChatMessage("user", menu.getSnack_or_tea() + "을 먹는다고 했을 때 섭취하는 사람에게 어떤 효능이 있을까? 위 아래 문단의 잡설은 하지 말고, 번호 리스트로 짧은 내용만 보여줘.")));
        if (multiChat.isBlank() || multiChat.isEmpty()) {
            multiChat = chatgptService.multiChat(List.of(new MultiChatMessage("user", menu.getSnack_or_tea() + "을 먹는다고 했을 때 섭취하는 사람에게 어떤 효능이 있을까? 위 아래 문단의 잡설은 하지 말고, 번호 리스트로 짧은 내용만 보여줘.")));
        }
        return new AiResDto(multiChat);
    }


    @Transactional
    public void saveResponse(String response, long user_id, Meals meals) {
        User id = userRepository.findById(user_id).orElseThrow(() -> new IllegalArgumentException("Not Found User Data In Database."));
        GPTResponse gptResponse = GPTResponse.builder()
                .gptAnswer(response)
                .user(id)
                .meals(meals)
                .build();
        responseRepository.save(gptResponse);
    }

    public AiResDto provideResponse(long user_id, Meals meals) {
        try {
            GPTResponse gptResponse = responseRepository.findByMealsAndUserId(meals, user_id);
            return new AiResDto(gptResponse.getGptAnswer());
        } catch (Exception e) {
            throw new IllegalArgumentException("Not Found Response Data In Database.");
        }
    }
}
