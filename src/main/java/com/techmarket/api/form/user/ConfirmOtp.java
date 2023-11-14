package com.techmarket.api.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class ConfirmOtp {
    @NotEmpty(message = "OPT can not be null.")
    @ApiModelProperty(name = "otp", required = true)
    private String otp;

    @NotEmpty(message = "Email can not be null.")
    @ApiModelProperty(name = "idHash", required = true)
    private String idHash;

}
