package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.order.OrderDto;
import com.techmarket.api.dto.product.ProductDto;
import com.techmarket.api.mapper.OrderMapper;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.Product;
import com.techmarket.api.model.User;
import com.techmarket.api.model.criteria.OrderCriteria;
import com.techmarket.api.repository.OrderRepository;
import com.techmarket.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController extends ABasicController{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserRepository userRepository;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('PR_L')")
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> getList(@Valid OrderCriteria orderCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<OrderDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<OrderDto>> responseListDto = new ResponseListDto<>();
        Page<Order> listOrder = orderRepository.findAll(orderCriteria.getCriteria(),pageable);
        responseListDto.setContent(orderMapper.fromEntityToListOrderDto(listOrder.getContent()));
        responseListDto.setTotalPages(listOrder.getTotalPages());
        responseListDto.setTotalElements(listOrder.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get list order success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('PR_V')")
    public ApiMessageDto<OrderDto> getOrder(@PathVariable("id") Long id) {
        ApiMessageDto<OrderDto> apiMessageDto = new ApiMessageDto<>();

        Order order = orderRepository.findById(id).orElse(null);
        if (order==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(orderMapper.fromOrderToDto(order));
        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Get order success.");
        return  apiMessageDto;
    }

    @GetMapping(value = "/my-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> getMyOrder(Pageable pageable) {

        ApiMessageDto<ResponseListDto<List<OrderDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<OrderDto>> responseListDto = new ResponseListDto<>();

        Long accountId = getCurrentUser();
        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Page<Order> orderPage = orderRepository.findAllByUserId(user.getId(),pageable);
        responseListDto.setContent(orderMapper.fromEntityToListOrderDto(orderPage.getContent()));
        responseListDto.setTotalPages(orderPage.getTotalPages());
        responseListDto.setTotalElements(orderPage.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get my-order success");

        return apiMessageDto;
    }

//    @GetMapping(value = "/update-status", produces = MediaType.APPLICATION_JSON_VALUE)
////    @PreAuthorize("hasRole('PR_V')")
//    public ApiMessageDto<String> updateOrder(@PathVariable("id") Long id) {
//        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
//
//        Order order = orderRepository.findById(id).orElse(null);
//        if (order ==null)
//        {
//            apiMessageDto.setResult(false);
//            apiMessageDto.setMessage("Not found order");
//            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
//            return apiMessageDto;
//        }
//
//
//    }

}
