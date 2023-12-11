package com.techmarket.api.dto.order;

import com.techmarket.api.dto.orderDetail.OrderDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderForGuestDto {

    private List<OrderDetailDto> content;
    private OrderDto orderDto;
    private long totalPages;
    private long totalElements;
}
