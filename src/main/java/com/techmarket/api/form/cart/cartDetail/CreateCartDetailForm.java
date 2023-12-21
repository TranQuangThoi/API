package com.techmarket.api.form.cart.cartDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateCartDetailForm {
    @NotNull(message = "quantity cant not be null")
    @ApiModelProperty(name = "quantity", required = true)
    private Integer quantity;
    @NotNull(message = "variantId cant not be null")
    @ApiModelProperty(name = "variantId", required = true)
    private Long variantId;
}
