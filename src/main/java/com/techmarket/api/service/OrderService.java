package com.techmarket.api.service;


import com.techmarket.api.controller.ABasicController;
//import com.techmarket.api.cookie.cookie;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.form.order.AddProductToOrder;
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


    public void handleOrder(ProductVariant productVariantInStock, AddProductToOrder addProductToOrder, OrderDetail orderDetail)
    {
        orderDetail.setProductVariantId(productVariantInStock.getId());
        orderDetail.setAmount(addProductToOrder.getQuantity());
        orderDetail.setPrice(productVariantInStock.getPrice()*addProductToOrder.getQuantity());
        orderDetail.setColor(productVariantInStock.getColor());
        orderDetail.setName(productVariantInStock.getProduct().getName());
        orderDetail.setProduct_Id(productVariantInStock.getProduct().getId());
        orderDetailRepository.save(orderDetail);

        productVariantInStock.setTotalStock(productVariantInStock.getTotalStock() -addProductToOrder.getQuantity());
        productVariantRepository.save(productVariantInStock);
        Product product = productRepository.findById(productVariantInStock.getProduct().getId()).orElse(null);
        product.setSoldAmount(product.getSoldAmount() + addProductToOrder.getQuantity());
        product.setTotalInStock(product.getTotalInStock() - addProductToOrder.getQuantity());
        productRepository.save(product);

    }
    public void handleVoucher(Long voucherId,Order order)
    {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if (voucher.getAmount() != null && !voucher.getAmount().equals(Integer.valueOf(0)))
        {
            order.setVoucherId(voucherId);
            voucher.setAmount(voucher.getAmount()-1);
            voucherRepository.save(voucher);
        }
    }


}

