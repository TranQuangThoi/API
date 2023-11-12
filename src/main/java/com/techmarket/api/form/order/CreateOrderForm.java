package com.techmarket.api.form.order;

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
    @NotEmpty(message = "payment method can not be empty")
    @ApiModelProperty(name = "payment method", required = true)
    private Integer paymentMethod;
    @NotNull(message = "productVariantId can not be null")
    @ApiModelProperty(name = "productVariantId", required = true)
    private Long provariantId;
    @NotNull(message = "amount can not be null")
    @ApiModelProperty(name = "amount", required = true)
    private Integer amount;
    @ApiModelProperty(name = "note")
    private String note;


}
