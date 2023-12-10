package com.techmarket.api.form.transaction;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreatePaymentForm {

    @NotNull(message = "OrderId cant not be null")
    @ApiModelProperty(name = "OrderId", required = true)
    private Long orderId;

//    @NotNull(message = "amount cant not be null")
//    @ApiModelProperty(name = "amount", required = true)
//    private Long amount;

    private String urlSuccess;
    private String urlCancel;


}
