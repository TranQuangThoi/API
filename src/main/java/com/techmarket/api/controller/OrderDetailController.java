package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.orderDetail.OrderDetailDto;
import com.techmarket.api.mapper.OrderDetailMapper;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.OrderDetail;
import com.techmarket.api.repository.OrderDetailRepository;
import com.techmarket.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/order-detail")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderDetailController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @GetMapping(value = "/get-by-order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrderDetailDto>>> getByOrder(@PathVariable("id") Long id, Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<OrderDetailDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<OrderDetailDto>> responseListDto = new ResponseListDto<>();
        Order order = orderRepository.findById(id).orElse(null);
        if (order==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Page<OrderDetail> orderDetail = orderDetailRepository.findAllByOrderId(id,pageable);
        responseListDto.setContent(orderDetailMapper.fromEntityToListOrderDetailDto(orderDetail.getContent()));
        responseListDto.setTotalPages(orderDetail.getTotalPages());
        responseListDto.setTotalElements(orderDetail.getTotalElements());

        apiMessageDto.setMessage("get order detail success");
        apiMessageDto.setData(responseListDto);
        return apiMessageDto;
    }
    @GetMapping(value = "/get-by-phone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrderDetailDto>>> getByPhoneAndOrder(@RequestParam("phone") String phone, @RequestParam("orderId") Long orderId, Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<OrderDetailDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<OrderDetailDto>> responseListDto = new ResponseListDto<>();
        Order order = orderRepository.findByPhoneAndId(phone,orderId);
        if (order==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Order or phone not found");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Page<OrderDetail> list = orderDetailRepository.findAllByOrderIdAndPhone(phone,orderId,pageable);
        responseListDto.setContent(orderDetailMapper.fromEntityToListOrderDetailDto(list.getContent()));
        responseListDto.setTotalElements(list.getTotalElements());
        responseListDto.setTotalPages(list.getTotalPages());

        apiMessageDto.setMessage("get order detail success");
        apiMessageDto.setData(responseListDto);
        return apiMessageDto;
    }
}
