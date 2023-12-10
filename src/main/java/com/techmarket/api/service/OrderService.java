package com.techmarket.api.service;


import com.techmarket.api.controller.ABasicController;
import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.cart.CartDto;
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

    public void handleProduct(ProductVariant productVariant,CartDto item,OrderDetail orderDetail)
    {
        orderDetail.setProductVariantId(productVariant.getId());
        orderDetail.setAmount(item.getQuantity());
        orderDetail.setPrice(item.getPrice());
        orderDetail.setColor(productVariant.getColor());
        orderDetail.setName(productVariant.getProduct().getName());
        orderDetail.setProduct_Id(productVariant.getProduct().getId());
        orderDetailRepository.save(orderDetail);

        productVariant.setTotalStock(productVariant.getTotalStock() -item.getQuantity());
        productVariantRepository.save(productVariant);
        Product product = productRepository.findById(productVariant.getProduct().getId()).orElse(null);
        product.setSoldAmount(product.getSoldAmount() + item.getQuantity());
        product.setTotalInStock(product.getTotalInStock() - item.getQuantity());
        productRepository.save(product);

    }

}

