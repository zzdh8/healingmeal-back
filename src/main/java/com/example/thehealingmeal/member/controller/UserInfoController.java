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
    public ResponseEntity<String> searchId(@RequestParam String name, @RequestParam String email){
        UserSearchDto loginId = searchService.searchId(name, email);
        return new ResponseEntity<>(loginId.getLoginId(), HttpStatus.OK);
    }

    //비밀번호 찾기
    //이름과 이메일, 아이디를 파라미터로 받음.
    @PostMapping("/user/search/pwd")
    public ResponseEntity<String> searchPwd(@RequestBody UserSearchDto userSearchDto){
        return new ResponseEntity<>(searchService.searchPassword(userSearchDto),HttpStatus.OK);
    }

    //비밀번호 변경 password modified
    @PutMapping("/{user_id}/change/pwd")
    public ResponseEntity<String> changePwd(@RequestBody PwdChangeDto pwdChangeDto, @PathVariable String user_id){
        try {
            userInfoModify.changePwd(pwdChangeDto, user_id);
            return new ResponseEntity<>("changing password is success.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("changing password is failed. please, check your input value.", HttpStatus.BAD_REQUEST);
        }

}