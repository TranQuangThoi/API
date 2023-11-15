package com.techmarket.api.form.review;

import com.techmarket.api.validation.ReviewStar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class UpdateReviewForm {

    @NotNull(message = "Id can not empty")
    @ApiModelProperty(name = "Id", required = true)
    private Long id;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(name = "status", required = true)
    private Integer status;

    @NotNull(message = "star cannot be null")
    @ApiModelProperty(name = "star", required = true)
    @ReviewStar
    private Integer star;

    @ApiModelProperty(name = "message")
    private String message;
}
