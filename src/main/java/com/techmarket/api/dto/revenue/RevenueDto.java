package com.techmarket.api.dto.revenue;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDto {

    private Double revenue;
    private Long amount;
    private Long amountUser;
    private Long amountProduct;
}
