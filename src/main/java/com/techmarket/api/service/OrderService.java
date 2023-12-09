package com.techmarket.api.service;


import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.model.*;
import com.techmarket.api.repository.OrderDetailRepository;
import com.techmarket.api.repository.ProductRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import com.techmarket.api.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrderService {

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

}
