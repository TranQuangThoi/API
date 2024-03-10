package com.techmarket.api.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class AccountForUser {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "kind")
    private int kind;
    @ApiModelProperty(name = "username")
    private String username;
    @ApiModelProperty(name = "phone")
    private String phone;
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "fullName")
    private String fullName;
    @ApiModelProperty(name = "lastLogin")
    private Date lastLogin;
    @ApiModelProperty(name = "avatar")
    private String avatar;
    private Boolean isSuperAdmin;
    private Integer status;
}
