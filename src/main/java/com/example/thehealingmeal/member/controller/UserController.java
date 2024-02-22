package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.member.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserRepository userRepository;

    /*
    인증 테스트 authentication test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test(Authentication authentication) {
        return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("index", HttpStatus.OK);
    }


    //login confirm
    @GetMapping("/user/confirm")
    public ResponseEntity<String[]> confirm(HttpServletRequest request){
        String[] userInfo = { userService.loginConfirmUserName(request),
                String.valueOf(userService.loginConfirmUserID(request))
        };
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    //pwd confirm
    @GetMapping("/user/confirm/pwd")
    public ResponseEntity<String> confirmPwd(HttpServletRequest request) {
        User user = userRepository.findByLoginId(request.getUserPrincipal().getName()).orElseThrow(() -> new RuntimeException("Unbelieved error. principal is not found."));
        return new ResponseEntity<>(user.getEmail(), HttpStatus.OK);
    }
}