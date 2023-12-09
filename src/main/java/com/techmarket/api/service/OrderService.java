package com.techmarket.api.service;


import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.controller.ABasicController;
import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.model.*;
import com.techmarket.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrderService extends ABasicController {

    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private cookie cookie;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    public void canelOrder(Long orderId)
    {
        List<OrderDetail> orderDetail = orderDetailRepository.findAllByOrderId(orderId);
        for (OrderDetail item : orderDetail)
        {
            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId()).orElse(null);
            productVariant.setTotalStock(productVariant.getTotalStock() + item.getAmount());
            Product product = productRepository.findById(item.getProduct_Id()).orElse(null);
            product.setTotalInStock(product.getTotalInStock() + item.getAmount());
            product.setSoldAmount(product.getSoldAmount() - item.getAmount());
            productVariantRepository.save(productVariant);
            productRepository.save(product);
        }

    }

    public ApiMessageDto<String> createOrder(List<CartDto> cartDtos, CreateOrderForm createOrderForm)
    {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if (cartDtos.size() == 0) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Hiện không có sản phẩm nào trong giỏ hàng !!!");
            apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Order order = new Order();
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

        Double totalPrice = 0.0;
        for (CartDto item : cartDtos) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId()).orElse(null);
            if (productVariant == null) {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Not found product variant");
                apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
                return apiMessageDto;
            }

            if (productVariant.getTotalStock().equals(0) || productVariant.getTotalStock() < item.getQuantity()) {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Product variant sold out or quantity exceeded");
                apiMessageDto.setCode(ErrorCode.PRODUCT_VARIANT_ERROR_NOT_FOUND);
                return apiMessageDto;
            }

            orderDetail.setProductVariantId(productVariant.getId());
            orderDetail.setAmount(item.getQuantity());
            orderDetail.setPrice(item.getPrice());
            orderDetail.setColor(productVariant.getColor());
            orderDetail.setName(productVariant.getProduct().getName());
            orderDetail.setProduct_Id(productVariant.getProduct().getId());
            orderDetailRepository.save(orderDetail);

            totalPrice += item.getPrice();

            // Cập nhật tồn kho sản phẩm và số lượng đã bán
            productVariant.setTotalStock(productVariant.getTotalStock() - item.getQuantity());
            productVariantRepository.save(productVariant);

            Product product = productRepository.findById(productVariant.getProduct().getId()).orElse(null);
            if (product != null) {
                product.setSoldAmount(product.getSoldAmount() + item.getQuantity());
                product.setTotalInStock(product.getTotalInStock() - item.getQuantity());
                productRepository.save(product);
            }
        }

        if (createOrderForm.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(createOrderForm.getVoucherId()).orElse(null);
            if (voucher==null)
            {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Not found voucher");
                apiMessageDto.setCode(ErrorCode.VOUCHER_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            if (voucher.getAmount() != null && !voucher.getAmount().equals(Integer.valueOf(0)))
            {
                order.setVoucherId(createOrderForm.getVoucherId());
                voucher.setAmount(voucher.getAmount()-1);
                voucherRepository.save(voucher);
            }
        }

        order.setTotalMoney(totalPrice);
        orderRepository.save(order);



        apiMessageDto.setMessage("create order success");
        return apiMessageDto;
    }

}

