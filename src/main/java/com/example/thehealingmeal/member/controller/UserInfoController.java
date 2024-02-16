package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.dto.PwdChangeDto;
import com.example.thehealingmeal.member.dto.UserSearchDto;
import com.example.thehealingmeal.member.service.SearchService;
import com.example.thehealingmeal.member.service.UserInfoModify;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserInfoController {
    private final SearchService searchService;
    private final UserInfoModify userInfoModify;

    //아이디 찾기
    //이름과 이메일을 파라미터로 받음.
    @GetMapping("/user/search/id")
    public ResponseEntity<String> searchId(String name, String email){
        UserSearchDto loginId = searchService.searchId(name, email);
        return new ResponseEntity<>(loginId.getLoginId(), HttpStatus.OK);
    }

    //비밀번호 찾기
    //이름과 이메일, 아이디를 파라미터로 받음.
    @GetMapping("/user/search/pwd")
    public ResponseEntity<String> searchPwd(@RequestBody UserSearchDto userSearchDto){
        return new ResponseEntity<>(searchService.searchPassword(userSearchDto),HttpStatus.OK);
    }

    //비밀번호 변경
    //현재 비밀번호와 변경하고자 하는 비밀번호를 파라미터로 받음.
//    @PutMapping("/change/pwd")
//    public ResponseEntity<String> changePwd(@RequestBody PwdChangeDto pwdChangeDto, HttpServletRequest request){
//        userInfoModify.changePwd(pwdChangeDto, request.getUserPrincipal().getName());
//        request.authenticate(response -> {})
//        return new ResponseEntity<>("changing password is success.", HttpStatus.OK);
//    }

    @PutMapping("/{user_id}/change/pwd")
    public ResponseEntity<String> changePwd(@RequestBody PwdChangeDto pwdChangeDto, @PathVariable String user_id){
        userInfoModify.changePwd(pwdChangeDto, user_id);
        return new ResponseEntity<>("changing password is success.", HttpStatus.OK);
    }
}