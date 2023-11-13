package com.techmarket.api.form.order;

import com.techmarket.api.validation.OrderState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMyOrderForm {

    @NotNull(message = "id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "state")
    @OrderState
    private Integer state;

}
