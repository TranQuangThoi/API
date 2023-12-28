package com.techmarket.api.form.cart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateCartForm {
    @NotEmpty(message = "userId cant not be null")
    @ApiModelProperty(name = "userId", required = true)
    private Long userId;
}
