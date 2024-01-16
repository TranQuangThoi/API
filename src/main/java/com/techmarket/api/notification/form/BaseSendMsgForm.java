package com.techmarket.api.notification.form;

import lombok.Data;

@Data
public class BaseSendMsgForm<T> {
    private String cmd;
    private String app;
    private T data;
}
