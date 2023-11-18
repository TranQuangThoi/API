package com.techmarket.api.form.review;

import com.techmarket.api.validation.ReviewStar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class CreateReviewForm {
    @NotNull(message = "productId can not empty")
    @ApiModelProperty(name = "productId", required = true)
    private Long productId;

    @NotNull(message = "star cannot be null")
    @ApiModelProperty(name = "star", required = true)
    @ReviewStar
    private Integer star;

    @ApiModelProperty(name = "message")
    private String message;
}
