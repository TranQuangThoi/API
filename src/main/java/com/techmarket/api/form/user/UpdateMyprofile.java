package com.techmarket.api.form.user;

import com.techmarket.api.validation.GenderKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class UpdateMyprofile {

    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String fullName;
    @NotEmpty(message = "phone cant not be null")
    @ApiModelProperty(name = "phone", required = true)
    private String phone;
    @Email
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "birthday")
    private Date birthday;
    @ApiModelProperty(name = "newPassword")
    private String newPassword;
    @NotEmpty(message = "oldPassword cannot be empty")
    @ApiModelProperty(name = "oldPassword",required = true)
    private String oldPassword;
    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;
    @ApiModelProperty(name = "gender")
    @GenderKind
    private Integer gender;
}
