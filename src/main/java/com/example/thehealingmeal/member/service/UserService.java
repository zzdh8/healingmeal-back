package com.example.thehealingmeal.member.service;


import com.example.thehealingmeal.member.domain.PrincipalDetail;
import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.execption.InvalidUserException;
import com.example.thehealingmeal.member.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found User"));
        return new PrincipalDetail(user);
    }

    //유저 번호 반환
    public Long userID(String authenticatedPrincipal) {
        User user = userRepository.findByLoginId(authenticatedPrincipal).orElseThrow(() -> new RuntimeException("Unbelieved error. principal is not found."));
        return user.getId();
    }

    //로그인 상태 확인 메서드
    //로그인 상태면 로그인한 유저의 이름을 반환하고, 로그인 상태 확인 결과을 반환.
    public String loginConfirmUserName(String id) throws InvalidUserException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new InvalidUserException("로그인 상태가 아닙니다."));
        if (user != null) {
            return user.getName();
        }
        throw new InvalidUserException("not login");
    }

    public long loginConfirmUserID(String id) throws InvalidUserException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new InvalidUserException("로그인 상태가 아닙니다."));
        if (user != null) {
            return user.getId();
        }
        throw new InvalidUserException("not login");
    }
}