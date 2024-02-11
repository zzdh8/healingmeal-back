package com.example.thehealingmeal.ai.responseRepository;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Meals;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GPTResponse {
    @Id
    @Column(name = "response_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    private Meals meals;

    @NotNull
    @NotEmpty
    @Column(name = "gpt_answer", columnDefinition = "TEXT")
    private String gptAnswer;

}
