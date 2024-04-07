package com.techmarket.api.notification.dto;

import lombok.Data;

@Data
public class ReviewNotification {

    private Long notificationId;
    private Long productId;
    private Long reviewId;
}
