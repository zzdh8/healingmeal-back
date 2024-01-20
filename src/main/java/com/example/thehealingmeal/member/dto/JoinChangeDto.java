package com.example.thehealingmeal.member.dto;


import com.example.thehealingmeal.member.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinChangeDto {

    private String name;

    private String email;

    private String birthDate;

    private Gender gender;

    private String phoneNumber;
}
