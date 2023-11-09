package com.techmarket.api.dto.service;

import com.techmarket.api.dto.ABasicAdminDto;
import com.techmarket.api.dto.account.AccountDto;
import com.techmarket.api.model.Group;
import lombok.Data;

@Data
public class ServiceDto extends ABasicAdminDto {
    private String serviceName;
    private Group group;
    private String logoPath;
    private String bannerPath;
    private String hotline;
    private String settings;
    private String lang;
    private Integer status;
    private AccountDto accountDto;

    private String tenantId;
}
