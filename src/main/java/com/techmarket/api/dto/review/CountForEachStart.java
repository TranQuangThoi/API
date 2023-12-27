package com.techmarket.api.dto.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountForEachStart {
    @ApiModelProperty(name = "star")
    private Integer star;
    @ApiModelProperty(name = "amount")
    private Long amount;
}
