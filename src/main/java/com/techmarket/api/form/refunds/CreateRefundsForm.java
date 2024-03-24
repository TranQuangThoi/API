package com.techmarket.api.form.refunds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class CreateRefundsForm {

    @NotNull(message = "orderId cant not be null")
    @ApiModelProperty(name = "orderId", required = true)
    private Long orderId;

    @ApiModelProperty(name = "cash", required = true)
    private Boolean cash;

    @ApiModelProperty(name = "bank")
    private String bank;

    @ApiModelProperty(name = "accountNumber")
    private String accountNumber;

    @ApiModelProperty(name = "fee")
    private Double fee;

    @ApiModelProperty(name = "phone")
    private String phone;

    @ApiModelProperty(name = "name")
    private String name;







}
