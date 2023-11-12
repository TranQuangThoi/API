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

    @ApiModelProperty(name = "expect receive date")
    private Date expectedDeliveryDate;

    @NotNull(message = " is Delivery not be null")
    @ApiModelProperty(name = "isDelivery", required = true)
    private Boolean isDelivery;

    @NotNull(message = " is Paid not be null")
    @ApiModelProperty(name = "isPaid", required = true)
    private Boolean isPaid;

}
