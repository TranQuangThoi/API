package com.techmarket.api.dto.user;

import com.techmarket.api.dto.account.AccountAutoCompleteDto;
import lombok.Data;

@Data
public class UserAutoCompleteDto {

    private Long id;
    private AccountAutoCompleteDto accountAutoCompleteDto;
}
