package com.techmarket.api.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long productVariantId;
    private Integer quantity;
    private Double price;
    private String name;
    private String color;
    private String image;
}
