package com.techmarket.api.form.order;

import com.techmarket.api.validation.OrderState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateOrder {

    @NotNull(message = "id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotNull(message = "state can not be null")
    @ApiModelProperty(name = "state", required = true)
    @OrderState
    private Integer state;

    @ApiModelProperty(name = "expect receive date")
    private Date expectedDeliveryDate;

    @NotNull(message = " is Paid not be null")
    @ApiModelProperty(name = "isPaid", required = true)
    private Boolean isPaid;

}
