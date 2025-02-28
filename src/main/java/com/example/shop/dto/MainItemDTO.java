package com.example.shop.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MainItemDTO {

    private Long id; // 상품번호

    private String itemNm; // 상품명

    private String itemDetail; // 상품 설명

    private String imgUrl; // 대표이미지

    private Integer price; // 상품가격


}
