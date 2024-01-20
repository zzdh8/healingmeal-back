package com.example.thehealingmeal.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PwdChangeDto {
    @NotBlank
    @NotNull
    String nowPwd;
    @NotBlank
    @NotNull
    String changePwd;
}
