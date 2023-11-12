package com.techmarket.api.form.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateOrder {

    @NotNull(message = "id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotNull(message = "status can not be null")
    @ApiModelProperty(name = "status", required = true)
    private Integer status;

    @NotNull(message = "expect receive date can not be null")
    @ApiModelProperty(name = "expect receive dat", required = true)
    private Date expectedReveiveDate;
}
