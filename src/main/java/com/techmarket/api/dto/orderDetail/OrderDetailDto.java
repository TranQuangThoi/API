package com.techmarket.api.dto.orderDetail;

import lombok.Data;


@Data
public class OrderDetailDto {

    private Long id;
    private Long productVariantId;
    private String name;
    private String color;
    private Integer amount;
    private Double price;
    private String image;
    private Long productId;
    private Boolean isReviewed;
}
