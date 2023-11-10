package com.techmarket.api.dto.user;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.account.AccountDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends ABasicAdminDto {

    private Long id;
    private Date birthday;
    private AccountDto account;
    private Integer gender;

}
