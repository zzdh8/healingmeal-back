package com.example.thehealingmeal.member.controller;

import com.example.thehealingmeal.member.dto.TotalDto;
import com.example.thehealingmeal.member.service.TotalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TotalDataController {
    private final TotalDataService totalDataService;

    @GetMapping("/{userId}/totalData")
    public ResponseEntity<TotalDto> totalDto (@PathVariable Long userId){
        return new ResponseEntity<>(totalDataService.totalData(userId), HttpStatus.OK);
    }
}
