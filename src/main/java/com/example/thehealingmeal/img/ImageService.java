package com.example.thehealingmeal.img;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Value("${bucket-name}")
    private String bucketName;
    private final Storage storage;
    public ImageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String getMainDishImgInfo(String imageName) throws IllegalAccessException {
        // Google Cloud Storage 버킷과 이미지 이름 지정.
        String imageFullName = imageName + ".jpg";

        // 이미지를 가져옵니다.
        Blob blob = storage.get(bucketName, imageFullName);
        String metadata = blob.getContentType();
        // 이미지 정보를 클라이언트에게 보냅니다.
        return "Image metadata: " + metadata + "\n" + "Image size: " + blob.getSize() + " bytes";
    }
}
