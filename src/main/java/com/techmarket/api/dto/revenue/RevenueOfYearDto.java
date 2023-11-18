package com.techmarket.api.dto.revenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueOfYearDto {

    private Double revenue;
    private Integer month;
}
