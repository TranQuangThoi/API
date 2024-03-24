package com.techmarket.api.dto.refunds;

import lombok.Data;

@Data
public class RefundsDto {

    private Long id;
    private Long orderId;
    private Integer state;
    private Boolean cash;
    private String bank;
    private String accountNumber;
    private Double fee;
    private String phone;
    private String name;
}
