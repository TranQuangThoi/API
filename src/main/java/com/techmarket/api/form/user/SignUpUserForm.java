package com.techmarket.api.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@ApiModel
public class SignUpUserForm {

    @ApiModelProperty(name = "phone")
    private String phone;
    @ApiModelProperty(name = "email",required = true)
    @Email
    private String email;
    @NotEmpty(message = "user name can't not be null")
    @ApiModelProperty(name = "username",required = true)
    private String userName;
    @NotEmpty(message = "password cant not be null")
    @ApiModelProperty(name = "password", required = true)
    private String password;
    @NotEmpty(message = "fullName cant not be null")
    @ApiModelProperty(name = "fullName",example = "Tam Nguyen",required = true)
    private String fullName;
    private String avatarPath;
    @ApiModelProperty(name = "birthday")
    private Date birthday;

}
