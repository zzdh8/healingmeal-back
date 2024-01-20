package com.example.thehealingmeal.menu.service;

import com.example.thehealingmeal.menu.domain.SnackURL;
import com.example.thehealingmeal.menu.domain.repository.SnackUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnackUrlService {
    private final SnackUrlRepository snackUrlRepository;

    //간식 이미지 url을 원활하게 가져오기 위한 자동 테이블 생성
    @Transactional
    public void urlSave(){
        List<String> snackUrl = List.of("그릭요거트.jpeg",
                "당근사과 주스.jpeg",
                "라임티 아이스.jpg",
                "리얼 사과주스.jpeg",
                "리얼 수박 주스.jpeg",
                "밀싹 케일 주스.jpeg",
                "블루베리 요거트.jpeg",
                "사과 비트 착즙 주스.jpeg",
                "샌드위치.jpeg",
                "석류.jpeg",
                "식빵.jpeg",
                "오가닉 그릭 요거트 플레인.jpeg",
                "오가닉 프로틴 그릭 요거트 & 그래놀라.jpg",
                "오곡바나나주스.jpeg",
                "자몽요구르트.jpeg",
                "자몽 그린티.jpeg",
                "자몽 후레쉬 주스.jpg",
                "청포도 그린티.jpeg",
                "키위 생과일주스.jpeg",
                "토마토주스.jpeg",
                "호밀빵.jpeg"
        );

        for (String urls : snackUrl){
            SnackURL snackURL = SnackURL.builder()
                    .snackUrlName(urls).build();
            snackUrlRepository.save(snackURL);
        }
    }

}
