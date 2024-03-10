package com.techmarket.api.dto.user;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.account.AccountDto;
import com.techmarket.api.dto.account.AccountForUser;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends ABasicAdminDto {

    private Long id;
    private Date birthday;
    private AccountForUser account;
    private Integer gender;
    private Integer point;
    private Integer memberShip;

}
