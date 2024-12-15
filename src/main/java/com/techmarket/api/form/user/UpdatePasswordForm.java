package com.techmarket.api.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdatePasswordForm {
    @ApiModelProperty(name = "newPassword")
    private String newPassword;
    @ApiModelProperty(name = "oldPassword")
    private String oldPassword;

}
