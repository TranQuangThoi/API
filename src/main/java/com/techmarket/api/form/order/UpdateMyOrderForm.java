package com.techmarket.api.form.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMyOrderForm {

    @NotNull(message = "id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotNull(message = "status can not be null")
    @ApiModelProperty(name = "status", required = true)
    private Integer status;

}
