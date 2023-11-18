package com.techmarket.api.form.order;

import com.techmarket.api.validation.OrderState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMyOrderForm {

    @NotNull(message = "id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "state")
    @OrderState
    private Integer state;
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
    @ApiModelProperty(name = "note")
    private String note;

}
