package com.techmarket.api.controller;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.ResponseListDto;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.dto.order.OrderDto;
import com.techmarket.api.exception.UnauthorizationException;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.form.order.UpdateMyOrderForm;
import com.techmarket.api.form.order.UpdateOrder;
import com.techmarket.api.mapper.OrderMapper;
import com.techmarket.api.model.*;
import com.techmarket.api.model.criteria.OrderCriteria;
import com.techmarket.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private ProductRepository productRepository;


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

        if (order.getState().equals(UserBaseConstant.ORDER_STATE_BEING_TRANSPOSTED))
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Orders that have been shipped cannot be canceled");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_CANCEL);
            return apiMessageDto;
        }
        if (updateOrder.getState()!=null)
        {
            if (!updateOrder.getState().equals(UserBaseConstant.ORDER_STATE_CANCELED))
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("just allow user canncel order");
                apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_CANCEL);
                return apiMessageDto;
            }
            order.setState(updateOrder.getState());
        }

        orderRepository.save(order);
        apiMessageDto.setMessage("update status success");
        return apiMessageDto;
    }
    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> createOrder(@Valid @RequestBody CreateOrderForm createOrderForm, BindingResult bindingResult
            , HttpServletRequest request , HttpServletResponse response)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CartDto> cartItems = cookie.getCartItemsFromCookie(request);

        if (cartItems.size()==0)
        {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Hiện không có sản phẩm nào trong giỏ hàng !!!");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
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
            }
        }
        orderRepository.save(order);
        Double totalPrice=0.0;
        for (CartDto item : cartItems)
        {

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId()).orElse(null);
            if (productVariant.getTotalStock().equals(0))
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Product variant sold out");
                apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            if(productVariant.getTotalStock() < item.getQuantity())
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("product quantity has been exceeded ,Please reduce product quantity");
                apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            orderDetail.setProductVariantId(productVariant.getId());
            orderDetail.setAmount(item.getQuantity());
            orderDetail.setPrice(item.getPrice());
            orderDetail.setColor(productVariant.getColor());
            orderDetail.setName(productVariant.getProduct().getName());
            orderDetailRepository.save(orderDetail);
            totalPrice += item.getPrice();

            productVariant.setTotalStock(productVariant.getTotalStock() -item.getQuantity());
            productVariantRepository.save(productVariant);
            Product product = productRepository.findById(productVariant.getProduct().getId()).orElse(null);
            product.setSoldAmount(product.getSoldAmount() + item.getQuantity());
            product.setTotalInStock(product.getTotalInStock() - item.getQuantity());
            productRepository.save(product);
        }
        order.setTotalMoney(totalPrice);
        orderRepository.save(order);
        cookie.clearCartCookie(request,response);
        apiMessageDto.setMessage("create order success");
        return apiMessageDto;
    }



}
