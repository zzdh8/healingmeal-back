package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.service.SearchService;
import com.example.thehealingmeal.member.service.UserInfoModify;
import com.example.thehealingmeal.member.dto.PwdChangeDto;
import com.example.thehealingmeal.member.dto.UserSearchDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInfoController {
    private final SearchService searchService;
    private final UserInfoModify userInfoModify;

    //아이디 찾기
    //이름과 이메일을 파라미터로 받음.
    @GetMapping("/user/search/id")
    public ResponseEntity searchId(String name, String email){
        UserSearchDto loginId = searchService.searchId(name, email);
        return new ResponseEntity<String>(loginId.getLoginId(), HttpStatus.OK);
    }

    //비밀번호 찾기
    //이름과 이메일, 아이디를 파라미터로 받음.
    @GetMapping("/user/search/pwd")
    public ResponseEntity searchPwd(String name, String email, String loginId){
        return new ResponseEntity<String>(searchService.searchPassword(name, email, loginId),HttpStatus.OK);
    }

    //비밀번호 변경
    //현재 비밀번호와 변경하고자 하는 비밀번호를 파라미터로 받음.
    @PatchMapping("/change/pwd")
    public ResponseEntity changePwd(@RequestBody PwdChangeDto pwdChangeDto, HttpServletRequest request){
        userInfoModify.changePwd(pwdChangeDto, request.getUserPrincipal().getName());
        return new ResponseEntity<String>("changing password is success.", HttpStatus.OK);
    }

}