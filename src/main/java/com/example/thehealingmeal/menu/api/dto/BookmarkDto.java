package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.MenuForUser;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkRequestDto {
    private MenuForUser menuForUsers;

    private User user;
}
