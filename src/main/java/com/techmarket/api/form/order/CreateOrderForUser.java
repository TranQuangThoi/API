package com.techmarket.api.form.order;

import com.techmarket.api.validation.PaymentKind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class CreateOrderForUser {
    @NotEmpty(message = "receiver name can not be empty")
    @ApiModelProperty(name = "reciver name", required = true)
    private String receiver;
    @NotNull(message = "addressId detail can not be empty")
    @ApiModelProperty(name = "addressId detail", required = true)
    private Long addressId;
    @NotNull(message = "payment method can not be empty")
    @ApiModelProperty(name = "payment method", required = true)
    @PaymentKind
    private Integer paymentMethod;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "voucherId")
    private Long voucherId;
    @ApiModelProperty(name = "email")
    @Email
    private String email;
    private List<AddProductToOrder> listOrderProduct;
}
