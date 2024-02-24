package com.example.thehealingmeal.gpt;

import com.example.thehealingmeal.gpt.dto.AiResDto;
import com.example.thehealingmeal.gpt.responseRepository.GPTResponse;
import com.example.thehealingmeal.gpt.responseRepository.ResponseRepository;
import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.menu.api.dto.MenuResponseDto;
import com.example.thehealingmeal.menu.api.dto.SnackOrTeaResponseDto;
import com.example.thehealingmeal.menu.domain.Meals;
import com.example.thehealingmeal.menu.service.MenuProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final ChatClient chatClient;
    private final MenuProvider menuProvider;
    private final UserRepository userRepository;
    private final ResponseRepository responseRepository;

    private ChatResponse callChat(String menu) {
        return chatClient.call(
                new Prompt(
                        menu + "을 먹는다고 했을 때 섭취하는 사람에게 어떤 효능이 있을까? 위 아래 문단의 잡설은 하지 말고, 각 음식에 대해 번호 리스트로 짧은 분량의 내용을 보여줘.",
                        OpenAiChatOptions.builder()
                                .withTemperature(0.4F)
                                .withFrequencyPenalty(0.7F)
                                .withModel("gpt-3.5-turbo")
                                .build()
                ));
    }

    //request answer to openai.
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<AiResDto> getAnswer(long user_id, Meals meals) {
        MenuResponseDto menu = menuProvider.provide(user_id, meals);
        List<String> names = List.of(menu.getMain_dish(), menu.getRice(), menu.getSideDishForUserMenu().toString());
        StringBuilder sentence = new StringBuilder();
        for (String name : names) {
            sentence.append(name);
            if (names.iterator().hasNext()) {
                sentence.append(", ");
            }
        }

        ChatResponse response = callChat(sentence.toString());
        if (response == null) {
            response = callChat(sentence.toString());
        }
        return CompletableFuture.completedFuture(new AiResDto(response.getResult().getOutput().getContent()));
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<AiResDto> getAnswerSnackOrTea(long user_id, Meals meals) {
        SnackOrTeaResponseDto menu = menuProvider.provideSnackOrTea(user_id, meals);
        String multiChat = callChat(menu.getSnack_or_tea()).getResult().getOutput().getContent();
        if (multiChat == null || multiChat.isBlank() || multiChat.isEmpty()) {
            multiChat = callChat(menu.getSnack_or_tea()).getResult().getOutput().getContent();
        }
        return CompletableFuture.completedFuture(new AiResDto(multiChat));
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
