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

    //로그인 상태 확인 메서드
    //로그인 상태면 로그인한 유저의 이름을 반환하고, 로그인 상태 확인 결과을 반환.
    public String loginConfirmUserName(HttpServletRequest request) throws InvalidUserException {
        Principal user = request.getUserPrincipal();
        if (user != null) {
            User loginUser = userRepository.findByLoginId(user.getName()).orElseThrow(() -> new InvalidUserException("로그인 상태가 아닙니다."));
            return loginUser.getName();
        }
        throw new InvalidUserException("not login");
    }

    public long loginConfirmUserID(HttpServletRequest request) throws InvalidUserException {
        Principal user = request.getUserPrincipal();
        if (user != null) {
            User loginUser = userRepository.findByLoginId(user.getName()).orElseThrow(() -> new InvalidUserException("로그인 상태가 아닙니다."));
            return loginUser.getId();
        }
        throw new InvalidUserException("not login");
    }
}