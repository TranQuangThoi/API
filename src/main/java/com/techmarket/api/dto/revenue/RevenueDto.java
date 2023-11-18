package com.techmarket.api.dto.revenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDto {

    private Double revenue;
    private Long amount;
}
