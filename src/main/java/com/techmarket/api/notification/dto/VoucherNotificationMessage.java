package com.techmarket.api.notification.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class VoucherNotificationMessage {
    private Long notificationId;
    private Long userId;
    private Long voucherId;
    private String title;
    private String content;
    private Integer percent;
    private Date expired;
    private Integer amount;
}
