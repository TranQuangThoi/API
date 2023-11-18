package com.techmarket.api.dto.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AmountReviewDto {
    @ApiModelProperty(name = "star")
    private Double star;
    @ApiModelProperty(name = "amount")
    private Long amount;
}
