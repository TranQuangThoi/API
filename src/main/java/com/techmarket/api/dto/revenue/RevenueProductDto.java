package com.techmarket.api.dto.revenue;

import lombok.Data;

@Data
public class RevenueProductDto {

    private String productName;
    private Integer amount;
    private Double totalRevenue;

}
