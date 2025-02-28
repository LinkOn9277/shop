package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(exclude = "item")
@NoArgsConstructor
@AllArgsConstructor
public class ImgEntity {
    // 사진을 저장시
    // 사진이 달려있는 참조하고 있는 테이블
    // 사진이 저장되어있는 경로
    // 경로 + 이미지 이름 (uuid)
    // 사진의 이름 (uuid 없는 실제 이름)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    private String imgName; // uuid 포함 된 파일 이름

    private String oriImgName; // 짱구라는 이미지 이름

    private String imgUrl; //C\:Users\woori\IdraProject\demo\demo

    private String repimgYn; // 대표 이미지 여부 Y일 경우 대표 이미지

    // 참조 대상
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
