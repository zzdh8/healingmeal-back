package com.example.thehealingmeal.img;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    //    @GetMapping("/image")
//    public String getImageUrl() {
//        String bucket_name = "hm-practice-bucket";
//        String object_name = "꽃게탕.jpg";
//        return "https://storage.googleapis.com/"+bucket_name+"/"+object_name;
//    }

    @Value("${bucket-name}")
    private String bucketName;
    private final Storage storage;
    public ImageController() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    //이미지 처리 테스트용 API
    @GetMapping("/test/image")
    public String getImageInfo() {
        // Google Cloud Storage 버킷과 이미지 이름을 지정합니다.\

        String imageName = "꽃게탕.jpg";

        // 이미지를 가져옵니다.
        Blob blob = storage.get(bucketName, imageName);

        // 이미지 정보를 클라이언트에게 보냅니다.
        return "Image size: " + blob.getSize() + " bytes";
    }





}