package com.example.thehealingmeal.menu.api.dto;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.menu.domain.Bookmark;
import com.example.thehealingmeal.menu.domain.MenuForUser;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkDto {
    private MenuForUser menuForUser;
    private User user;

    public BookmarkDto(Bookmark bookmark) {
        this.menuForUser = bookmark.getMenuForUser();
        this.user = bookmark.getUser();
    }
}
