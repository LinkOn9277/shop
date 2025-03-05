package com.example.shop.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long itemId;

    @Min(value = 1 , message = "최소 1개 이상 담아야합니다.")
    private int count;


}
