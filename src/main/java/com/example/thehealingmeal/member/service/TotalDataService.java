package com.example.thehealingmeal.member.service;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.dto.TotalDto;
import com.example.thehealingmeal.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalDataService {
    private final UserRepository userRepository;

    public TotalDto totalData(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        return TotalDto.fromUser(user);
    }
}
