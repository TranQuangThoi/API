package com.techmarket.api.dto.orderDetail;

import lombok.Data;


@Data
public class OrderDetailDto {

    private Long id;
    private Long productVariantId;
    private String color;
    private String name;
    private Integer amount;
    private Double price;
}
