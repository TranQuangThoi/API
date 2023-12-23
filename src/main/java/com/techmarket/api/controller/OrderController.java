package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
//import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.dto.order.CreateOrderDto;
import com.techmarket.api.dto.order.OrderDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.order.*;
import com.techmarket.api.mapper.OrderMapper;
import com.techmarket.api.model.*;
import com.techmarket.api.model.criteria.OrderCriteria;
import com.techmarket.api.repository.*;
import com.techmarket.api.service.EmailService;
import com.techmarket.api.service.OrderService;
import com.techmarket.api.service.UserBaseOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserBaseOTPService userBaseOTPService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OD_L')")
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
    @PreAuthorize("hasRole('OD_V')")
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
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> getMyOrder(OrderCriteria orderCriteria ,Pageable pageable) {

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
//        Page<Order> orderPage = orderRepository.findAllByUserId(user.getId(),pageable);
        orderCriteria.setUserId(user.getId());
        Page<Order> orderPage = orderRepository.findAll(orderCriteria.getCriteria(),pageable);
        responseListDto.setContent(orderMapper.fromEntityToListOrderDto(orderPage.getContent()));
        responseListDto.setTotalPages(orderPage.getTotalPages());
        responseListDto.setTotalElements(orderPage.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get my-order success");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OD_U')")
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
        if (order.getState().equals(UserBaseConstant.ORDER_STATE_CANCELED))
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Customer has been canceled order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_CANCELED);
            return apiMessageDto;
        }
        if (updateOrder.getState().equals(UserBaseConstant.ORDER_STATE_CANCELED))
        {
            orderService.canelOrder(updateOrder.getId());
        }

        orderMapper.fromUpdateToOrderEntity(updateOrder,order);
        orderRepository.save(order);
        apiMessageDto.setMessage("update status success");
        return apiMessageDto;
    }
    @PutMapping(value = "/update-my-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateMyOrder(@Valid @RequestBody UpdateMyOrderForm updateOrder , BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if (!isUser())
        {
            throw new UnauthorizationException("Not allowed to update order.");
        }
        Order order = orderRepository.findById(updateOrder.getId()).orElse(null);
        if (order ==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (order.getState().equals(UserBaseConstant.ORDER_STATE_COMPLETED)
                || order.getState().equals(UserBaseConstant.ORDER_STATE_CANCELED))
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("You cannot update your order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_UPDATE);
            return apiMessageDto;
        }
        orderMapper.fromUpdateMyOrderToEntity(updateOrder,order);
        orderRepository.save(order);
        apiMessageDto.setMessage("update status success");
        return apiMessageDto;
    }
    @PutMapping(value = "/cancel-my-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> cancelMyOrder(@Valid @RequestBody ChangeStateMyOrder changeStateMyOrder ,BindingResult bindingResult) {
        if (!isUser())
        {
            throw new UnauthorizationException("Not allowed to update order.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Order order = orderRepository.findById(changeStateMyOrder.getId()).orElse(null);
        if (order ==null)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Not found order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (order.getState().equals(UserBaseConstant.ORDER_STATE_COMPLETED)
                || order.getState().equals(UserBaseConstant.ORDER_STATE_CANCELED))
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("This status cannot be updated once canceled or completed");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_UPDATE);
            return apiMessageDto;
        }
        order.setState(UserBaseConstant.ORDER_STATE_CANCELED);
        orderService.canelOrder(changeStateMyOrder.getId());
        orderRepository.save(order);
        apiMessageDto.setMessage("Cancel order success");
        return apiMessageDto;
    }


    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CreateOrderDto> createOrder(@Valid @RequestBody CreateOrderForm createOrderForm, BindingResult bindingResult ) throws MessagingException {
        ApiMessageDto<CreateOrderDto> apiMessageDto = new ApiMessageDto<>();

        CreateOrderDto createOrderDto = new CreateOrderDto();
        Order order = orderMapper.fromCreateOrderToEntity(createOrderForm);
        order.setState(UserBaseConstant.ORDER_STATE_PENDING_CONFIRMATION);

        String tokenExist = getCurrentToken();
        if (tokenExist!=null)
        {
            Long accountId = getCurrentUser();
            if (accountId!=null)
            {
                User user = userRepository.findByAccountId(accountId).orElse(null);
                if (user==null)
                {
                    apiMessageDto.setResult(false);
                    apiMessageDto.setMessage("Not found user");
                    apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
                    return apiMessageDto;
                }
                order.setUser(user);
                Cart cart = cartRepository.findCartByUserId(user.getId());
                cartDetailRepository.deleteAllByCartId(cart.getId());
            }

        }
        order.setOrderCode(userBaseOTPService.genCodeOrder(7));
        orderRepository.save(order);
        orderService.createOrder(createOrderForm,order);
        createOrderDto.setOrderId(order.getId());
        apiMessageDto.setData(createOrderDto);
        apiMessageDto.setMessage("create order success");
        return apiMessageDto;
    }
    @PostMapping(value = "/create-for-user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CreateOrderDto> createOrderForUser(@Valid @RequestBody CreateOrderForUser createOrderForUser, BindingResult bindingResult ) throws MessagingException {
        ApiMessageDto<CreateOrderDto> apiMessageDto = new ApiMessageDto<>();
        CreateOrderDto createOrderDto = new CreateOrderDto();
        Order order = orderMapper.fromCreateOrderforUserToEntity(createOrderForUser);
        Address address = addressRepository.findById(createOrderForUser.getAddressId()).orElse(null);
        order.setProvince(address.getProvince().getName());
        order.setWard(address.getWard().getName());
        order.setDistrict(address.getDistrict().getName());
        order.setAddress(address.getAddress());
        order.setPhone(address.getPhone());
        order.setState(UserBaseConstant.ORDER_STATE_PENDING_CONFIRMATION);

        String tokenExist = getCurrentToken();
        if (tokenExist!=null)
        {
            Long accountId = getCurrentUser();
            if (accountId!=null)
            {
                User user = userRepository.findByAccountId(accountId).orElse(null);
                if (user==null)
                {
                    apiMessageDto.setResult(false);
                    apiMessageDto.setMessage("Not found user");
                    apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
                    return apiMessageDto;
                }
                order.setUser(user);
                Cart cart = cartRepository.findCartByUserId(user.getId());
                cartDetailRepository.deleteAllByCartId(cart.getId());
            }

        }
        order.setOrderCode(userBaseOTPService.genCodeOrder(7));
        orderRepository.save(order);
        orderService.createOrderforUser(createOrderForUser,order);
        createOrderDto.setOrderId(order.getId());
        apiMessageDto.setMessage("create order success");
        apiMessageDto.setData(createOrderDto);
        return apiMessageDto;
    }

    @GetMapping(value = "/get-order-phone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> getOrderByPhone(@RequestParam String phone,Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<OrderDto>>> apiMessageDto = new ApiMessageDto<>();
        ResponseListDto<List<OrderDto>> responseListDto= new ResponseListDto<>();
        Page<Order> orderPage = orderRepository.findAllByPhone(phone,pageable);
        responseListDto.setContent(orderMapper.fromEntityToListOrderDto(orderPage.getContent()));
        responseListDto.setTotalPages(orderPage.getTotalPages());
        responseListDto.setTotalElements(orderPage.getTotalElements());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("get order success");
        return apiMessageDto;
    }


}
