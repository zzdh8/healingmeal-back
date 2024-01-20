package com.example.thehealingmeal.member.dto;

import com.example.thehealingmeal.member.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    private String loginId;
    private String password;
    private Role role;

}
