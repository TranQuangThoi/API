package com.techmarket.api.dto.review;

import com.techmarket.api.dto.product.RateProductDto;
import lombok.Data;

@Data
public class UnRatedDto {

    private Long orderId;
    private RateProductDto rateProductDto;
}
