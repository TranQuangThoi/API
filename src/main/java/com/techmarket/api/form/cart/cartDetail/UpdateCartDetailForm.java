package com.techmarket.api.form.cart.cartDetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCartDetailForm {
    @NotNull(message = "cartDetailId cant not be null")
    @ApiModelProperty(name = "cartDetailId", required = true)
    private Long cartDetailId;
    @NotNull(message = "quantity cant not be null")
    @ApiModelProperty(name = "quantity", required = true)
    private Integer quantity;
}
