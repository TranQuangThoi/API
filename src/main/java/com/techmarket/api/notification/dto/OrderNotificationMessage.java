package com.techmarket.api.notification.dto;

import lombok.Data;

@Data
public class OrderNotificationMessage {

    private Long notificationId;
    private Long userId;
    private Long orderid;
    private String orderCode;
    private String title;
    private Integer stateOrder;

}
