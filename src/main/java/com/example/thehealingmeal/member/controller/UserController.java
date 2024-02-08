package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /*
    로그인 테스트
    Postman으로 테스트 시 raw가 아닌 form-data로 해야 한다.
     */
    //login authorization test
    @GetMapping("/success")
    public ResponseEntity<String> notSesstion() {
        String message = "Login Or Authorization success";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //로그아웃 성공 시
    @GetMapping("/successlogout")
    public ResponseEntity<String> logoutSesstion(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("logout success", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> success(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        session.setMaxInactiveInterval(1000);
        String user_id = userService.userID(authentication.getName()).toString();
        return new ResponseEntity<>(user_id, HttpStatus.OK);
    }

    @GetMapping("/user/confirm")
    public ResponseEntity<String[]> confirm(HttpServletRequest request) {
        long userId = userService.loginConfirmLong(request);
        String[] userInfo = {userService.loginConfirm(request), String.valueOf(userId)};
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}