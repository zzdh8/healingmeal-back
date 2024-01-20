package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.dto.CheckingPasswordDto;
import com.example.thehealingmeal.member.dto.JoinChangeDto;
import com.example.thehealingmeal.member.dto.JoinIdRequestDto;
import com.example.thehealingmeal.member.dto.JoinRequestDto;
import com.example.thehealingmeal.member.service.UserJoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserJoinController {

    private final UserJoinService userJoinService;

    public UserJoinController(UserJoinService userJoinService) {
        this.userJoinService = userJoinService;
    }

    @PostMapping("/user/join")
    public ResponseEntity<String> userJoin(@RequestBody JoinRequestDto joinRequestDto) {
        userJoinService.join(joinRequestDto);
        return new ResponseEntity<>("Membership registration completed!", HttpStatus.OK);
    }

    @PostMapping("user/join/id")
    public ResponseEntity<String> userJoinId(@RequestBody JoinIdRequestDto joinRequestDto) {
        boolean userId = userJoinService.isLoginIdDuplicate(joinRequestDto.getLoginId());
        if (!userId) {
            return new ResponseEntity<>("사용 가능한 아이디 입니다.", HttpStatus.OK);
        }
        return new ResponseEntity<>("아이디가 이미 사용 중입니다.", HttpStatus.NOT_FOUND);
    }
    // 회원 정보 수정 api
    @PatchMapping("/{userId}/change/join")
    public ResponseEntity<String> changeJoin(@PathVariable Long userId,@RequestBody JoinChangeDto joinChangeDto) {
        userJoinService.updateByUserId(userId,joinChangeDto);
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }
    // 비밀번호 확인 api
    @GetMapping("{userId}/check/password")
    public ResponseEntity<String> checkingPassword(@PathVariable Long userId, @RequestBody CheckingPasswordDto checkingPasswordDto) {
        if (userJoinService.checkingPassword(userId, checkingPasswordDto)) {
            return new ResponseEntity<>("비밀번호 일치", HttpStatus.OK);
        }
        return new ResponseEntity<>("비밀번호 불일치", HttpStatus.NOT_FOUND);
    }
}
