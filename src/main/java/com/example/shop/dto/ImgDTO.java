package com.example.shop.dto;

import com.example.shop.entity.Item;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImgDTO {

    private Long id;

    private String imgName; // uuid 포함 된 파일 이름

    private String oriImgName; // 짱구라는 이미지 이름

    private String imgUrl; //C\:Users\woori\IdraProject\demo\demo

    private String repimgYn; // 대표 이미지 여부 Y일 경우 대표 이미지

    // 참조 대상
    private ItemDTO itemDTO;


}
