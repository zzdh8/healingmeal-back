package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.menu.domain.Meals;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkRequestDto {
    private Meals meals;
}
