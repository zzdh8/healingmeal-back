package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.domain.User;
import com.example.thehealingmeal.member.repository.UserRepository;
import com.example.thehealingmeal.member.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    /*
    로그인 테스트
    Postman으로 테스트 시 raw가 아닌 form-data로 해야 한다.
     */
    //login authorization test
    @GetMapping("/successtest")
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

    @GetMapping("/success")
    public ResponseEntity<String> success (@RequestParam String user_id) {

            return new ResponseEntity<>(user_id, HttpStatus.OK);
    }

    @GetMapping("/user/confirm")
    public ResponseEntity<String[]> confirm(HttpServletRequest request) {
        String[] userInfo = { userService.loginConfirmUserName(request),
                String.valueOf(userService.loginConfirmUserID(request))
        };
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/user/confirm/pwd")
    public ResponseEntity<String> confirmPwd(HttpServletRequest request) {
        User user = userRepository.findByLoginId(request.getUserPrincipal().getName()).orElseThrow(() -> new RuntimeException("Unbelieved error. principal is not found."));
        return new ResponseEntity<>(user.getEmail(), HttpStatus.OK);
    }
}