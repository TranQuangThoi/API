package com.techmarket.api.form.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FeedbackForm {

    @ApiModelProperty(name = "message")
    private String message;
    @NotNull(message = "reviewId cannot be null")
    @ApiModelProperty(name = "reviewId" ,required = true)
    private Long reviewId;
}
