package com.techmarket.api.notification.dto;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class NotificationDto extends ABasicAdminDto {

    private Long idUser;
    private Integer state;
    private String kind;
    private String msg;
    private String refId;

}
