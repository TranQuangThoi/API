package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.dto.order.OrderDto;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.form.order.UpdateOrder;
import com.techmarket.api.mapper.OrderMapper;
import com.techmarket.api.model.*;
import com.techmarket.api.model.criteria.OrderCriteria;
import com.techmarket.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private cookie cookie;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private AccountRepository accountRepository;


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

    @PutMapping(value = "/update-Order", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('PR_V')")
    public ApiMessageDto<String> updateOrder(@Valid @RequestBody UpdateOrder updateOrder ,BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Order order = orderRepository.findById(updateOrder.getId()).orElse(null);
        if (order ==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        orderMapper.fromUpdateToOrderEntity(updateOrder,order);
        orderRepository.save(order);
        apiMessageDto.setMessage("update status success");
        return apiMessageDto;
    }
//    @PutMapping(value = "/update-my-order", produces = MediaType.APPLICATION_JSON_VALUE)
////    @PreAuthorize("hasRole('PR_V')")
//    public ApiMessageDto<String> updateMyOrder(@Valid @RequestBody UpdateMyOrderForm updateMyOrder , BindingResult bindingResult) {
//        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
//
//        Order order = orderRepository.findById(updateOrder.getId()).orElse(null);
//        if (order ==null)
//        {
//            apiMessageDto.setResult(false);
//            apiMessageDto.setMessage("Not found order");
//            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
//            return apiMessageDto;
//        }
//
//        orderMapper.fromUpdateToOrderEntity(updateOrder,order);
//        orderRepository.save(order);
//        apiMessageDto.setMessage("update status success");
//        return apiMessageDto;
//    }
    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> createOrder(@Valid @RequestBody CreateOrderForm createOrderForm, BindingResult bindingResult
            , HttpServletRequest request , HttpServletResponse response)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = cookie.getCartItemsFromCookie(request);

        Order order = orderMapper.fromCreateOrderToEntity(createOrderForm);
        order.setStatus(UserBaseConstant.STATUS_PENDING);

        String tokenExist = getCurrentToken();
        if (tokenExist!=null)
        {
            Long accountId = getCurrentUser();
            if (accountId!=null)
            {
                User user = userRepository.findByAccountId(accountId).orElse(null);
                if (user!=null)
                {
                    order.setUser(user);
                }
            }
        }
        orderRepository.save(order);
        Double totalPrice=0.0;
        for (CartDto item : cartItems)
        {

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId()).orElse(null);
            if (productVariant==null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Not found product variant");
                apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            orderDetail.setProductVariant(productVariant);
            orderDetail.setAmount(item.getQuantity());
            orderDetail.setPrice(item.getPrice());
            orderDetailRepository.save(orderDetail);
            totalPrice += item.getPrice();
        }
        order.setTotalMoney(totalPrice);
        orderRepository.save(order);
        cookie.clearCartCookie(request,response);
        apiMessageDto.setMessage("create order success");
        return apiMessageDto;
    }


}
