package com.example.thehealingmeal.member.service;


import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.dto.PwdChangeDto;
import com.example.thehealingmeal.member.execption.InvalidPasswordException;
import com.example.thehealingmeal.member.execption.InvalidUserException;
import com.example.thehealingmeal.member.execption.MismatchException;
import com.example.thehealingmeal.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserInfoModify {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //비밀번호 변경
    public void changePwd(PwdChangeDto pwdChangeDto, String loginId){
        if (validatePwd(pwdChangeDto.getChangePwd())){
            User user = userRepository.findByLoginId(loginId).orElseThrow(()-> new InvalidUserException("user not found in the user list table."));
            if(passwordEncoder.matches(pwdChangeDto.getNowPwd(),user.getPassword())){
                user.setPassword(passwordEncoder.encode(pwdChangeDto.getChangePwd()));
                userRepository.save(user);
            } else{
                throw new MismatchException("the password is mismatch.");
            }
        } else {
            throw new InvalidPasswordException("the password is invalid. please, follow the rule.");
        }
    }
    //-비밀번호 변경 전 유효성 검사
    boolean validatePwd(String changePwd){
        String REGEX = "^[0-9a-zA-Z]{6,8}$";
        return Pattern.matches(REGEX, changePwd);
    }

    //임시 비밀번호 발행
    protected String generateTemPwd(int length){

        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, length)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }
}
