package com.techmarket.api.form.review;

import com.techmarket.api.validation.ReviewStar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class CreateReviewForm {

    @NotNull(message = "star cannot be null")
    @ApiModelProperty(name = "star", required = true)
    @ReviewStar
    private Integer star;

    @ApiModelProperty(name = "message")
    private String message;
    @NotNull(message = "orderDetailId cannot be null")
    @ApiModelProperty(name = "orderDetailId" ,required = true)
    private Long orderDetailId;
}
