package com.techmarket.api.form.order;

import com.techmarket.api.validation.PaymentKind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class CreateOrderForm {

    @NotEmpty(message = "province can not be empty")
    @ApiModelProperty(name = "province", required = true)
    private String province;
    @NotEmpty(message = "ward can not be empty")
    @ApiModelProperty(name = "ward", required = true)
    private String ward;
    @NotEmpty(message = "district can not be empty")
    @ApiModelProperty(name = "district", required = true)
    private String district;
    @NotEmpty(message = "receiver name can not be empty")
    @ApiModelProperty(name = "reciver name", required = true)
    private String receiver;
    @NotEmpty(message = "phone can not be empty")
    @ApiModelProperty(name = "phone", required = true)
    private String phone;
    @NotEmpty(message = "address detail can not be empty")
    @ApiModelProperty(name = "address detail", required = true)
    private String address;
    @NotNull(message = "payment method can not be empty")
    @ApiModelProperty(name = "payment method", required = true)
    @PaymentKind
    private Integer paymentMethod;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "voucherId")
    private Long voucherId;


}
