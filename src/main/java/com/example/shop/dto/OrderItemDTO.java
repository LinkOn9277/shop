package com.example.shop.dto;

import com.example.shop.entity.OrderItem;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    // 주문 아이템


    private String itemNm; // 상품명

    private int count; // 주문수량

    private int orderPrice; // 주문금액

    private String imgUrl; // 상품 이미지 경로

    public OrderItemDTO(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

}
