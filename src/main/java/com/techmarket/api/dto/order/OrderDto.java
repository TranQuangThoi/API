package com.techmarket.api.dto.order;

import com.techmarket.api.dto.ABasicAdminDto;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDto extends ABasicAdminDto {

    private String province;
    private String ward;
    private String district;
    private String phone;
    private String address;
    private Integer paymentMethod;
    private String note;
    private String receiver;
    private Double totalMoney;
    private Long userId;
    private Boolean isPaid;
    private Date expectedDeliveryDate;
    private Integer state;
    private Long voucherId;
    private String orderCode;


}
