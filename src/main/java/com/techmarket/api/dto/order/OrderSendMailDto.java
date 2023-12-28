package com.techmarket.api.dto.order;

import lombok.Data;

@Data
public class OrderSendMailDto {

    private Integer quantity;
    private Double price;
    private String productName;
    private String color;
}
