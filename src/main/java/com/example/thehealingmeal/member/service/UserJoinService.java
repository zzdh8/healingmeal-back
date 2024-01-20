package com.example.thehealingmeal.member.service;

import com.example.thehealingmeal.member.domain.Role;
import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.dto.CheckingPasswordDto;
import com.example.thehealingmeal.member.dto.JoinChangeDto;
import com.example.thehealingmeal.member.dto.JoinRequestDto;
import com.example.thehealingmeal.member.execption.InvalidUserException;
import com.example.thehealingmeal.member.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserJoinService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public UserJoinService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Transactional
    public void join(JoinRequestDto joinRequestDto) {
        // 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(joinRequestDto.getPassword());

        User user = User.builder()
                .loginId(joinRequestDto.getLoginId())
                .password(encodedPassword)
                .name(joinRequestDto.getName())
                .email(joinRequestDto.getEmail())
                .gender(joinRequestDto.getGender())
                .birthDate(joinRequestDto.getBirthDate())
                .phoneNumber(joinRequestDto.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();
        validateDuplicateLoginId(user);
        validateDuplicatePhoneNumber(user);
        // 회원 정보 저장
        userRepository.save(user);
    }

    private void validateDuplicateLoginId(User user) {
        if (userRepository.existsByLoginId(user.getLoginId())) {
            throw new InvalidUserException("This ID is already taken.");
        }
    }

    private void validateDuplicatePhoneNumber(User user) {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new InvalidUserException("This phone number is already in use.");
        }
    }

    public boolean isLoginIdDuplicate(String loginId) {
        // 아이디 중복확인
        return userRepository.existsByLoginId(loginId);
    }

    @Transactional
    public void updateByUserId(Long userId, JoinChangeDto joinChangeDto) {

        User user = userRepository.findById(userId).orElseThrow();
        user.update(joinChangeDto);
        userRepository.save(user);
    }

    public boolean checkingPassword(Long userId, CheckingPasswordDto checkingPasswordDto) {
        User user = userRepository.findById(userId).orElseThrow();
        return passwordEncoder.matches(checkingPasswordDto.getPassword(), user.getPassword());
    }
}