package com.example.thehealingmeal.img.api;

import com.example.thehealingmeal.img.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {
    //Image Information Provide API
    private final ImageService imageService;
    @GetMapping("/test/image")
    public ResponseEntity<String> getImageInfo(@RequestParam("name") String name) {
        try {
            return ResponseEntity.ok(imageService.getMainDishImgInfo(name));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body("Image not found");
        }
    }





}