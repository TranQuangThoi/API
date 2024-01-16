package com.techmarket.api.service;


import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.controller.ABasicController;
import com.techmarket.api.form.order.AddProductToOrder;
import com.techmarket.api.form.order.CreateOrderForUser;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.model.*;
import com.techmarket.api.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
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
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;

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

    public void createOrder(CreateOrderForm createOrderForm , Order order) throws MessagingException {
        Double totalPrice=0.0;
        for (AddProductToOrder item : createOrderForm.getListOrderProduct())
        {
            Double totalPrice1Pro =0.0;
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            ProductVariant productVariantCheck = productVariantRepository.findById(item.getProductVariantId()).orElse(null);
            if (productVariantCheck==null)
            {
                throw new MessagingException("Not found product");
            }

            Product product = productRepository.findById(productVariantCheck.getProduct().getId()).orElse(null);
            if (product.getSaleOff()!=0.0)
            {
                totalPrice += (productVariantCheck.getPrice()-(productVariantCheck.getPrice()*product.getSaleOff())/100)*item.getQuantity();
                totalPrice1Pro += (productVariantCheck.getPrice()-(productVariantCheck.getPrice()*product.getSaleOff())/100)*item.getQuantity();


            }else {
                totalPrice += productVariantCheck.getPrice()* item.getQuantity();
                totalPrice1Pro += productVariantCheck.getPrice()* item.getQuantity();
            }
            handleOrder(productVariantCheck,item,orderDetail,totalPrice1Pro);
            item.setPrice(totalPrice);

        }
        if (createOrderForm.getVoucherId()!=null)
        {
           handleVoucher(createOrderForm.getVoucherId(),order,totalPrice);
        }
        order.setTotalMoney(totalPrice);
        orderRepository.save(order);
        if (createOrderForm.getEmail()!=null)
        {
            if (createOrderForm.getPaymentMethod().equals(UserBaseConstant.PAYMENT_KIND_CASH))
            {
                emailService.sendOrderToEmail(createOrderForm.getListOrderProduct(),order,order.getEmail());
            }
        }
    }
    public void createOrderforUser(CreateOrderForUser createOrderForm , Order order) throws MessagingException {
        Double totalPrice=0.0;
        for (AddProductToOrder item : createOrderForm.getListOrderProduct())
        {
            Double totalPrice1Pro =0.0;
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            ProductVariant productVariantCheck = productVariantRepository.findById(item.getProductVariantId()).orElse(null);
            if (productVariantCheck==null)
            {
                throw new MessagingException("Not found product");
            }

            Product product = productRepository.findById(productVariantCheck.getProduct().getId()).orElse(null);
            if (product.getSaleOff()!=0.0 && product.getSaleOff()!=null)
            {
                totalPrice += (productVariantCheck.getPrice()-(productVariantCheck.getPrice()*product.getSaleOff())/100)*item.getQuantity();
                totalPrice1Pro += (productVariantCheck.getPrice()-(productVariantCheck.getPrice()*product.getSaleOff())/100)*item.getQuantity();

            }else {
                totalPrice += productVariantCheck.getPrice()* item.getQuantity();
                totalPrice1Pro += productVariantCheck.getPrice()* item.getQuantity();

            }
            handleOrder(productVariantCheck,item,orderDetail,totalPrice1Pro);


        }
        if (createOrderForm.getVoucherId()!=null)
        {
            handleVoucher(createOrderForm.getVoucherId(),order,totalPrice);
        }
        order.setTotalMoney(totalPrice);
        orderRepository.save(order);
        if (createOrderForm.getEmail()!=null)
        {
            if (createOrderForm.getPaymentMethod().equals(UserBaseConstant.PAYMENT_KIND_CASH))
            {
                emailService.sendOrderToEmail(createOrderForm.getListOrderProduct(),order,order.getEmail());
            }
        }
    }

    public void handleOrder(ProductVariant productVariantInStock, AddProductToOrder addProductToOrder, OrderDetail orderDetail , Double totalPrice)
    {
        orderDetail.setProductVariantId(productVariantInStock.getId());
        orderDetail.setAmount(addProductToOrder.getQuantity());
        orderDetail.setPrice(totalPrice);
        orderDetail.setColor(productVariantInStock.getColor());
        orderDetail.setName(productVariantInStock.getProduct().getName());
        orderDetail.setProduct_Id(productVariantInStock.getProduct().getId());
        orderDetail.setImage(productVariantInStock.getImage());
        orderDetailRepository.save(orderDetail);

        productVariantInStock.setTotalStock(productVariantInStock.getTotalStock() -addProductToOrder.getQuantity());
        productVariantRepository.save(productVariantInStock);
        Product product = productRepository.findById(productVariantInStock.getProduct().getId()).orElse(null);
        product.setSoldAmount(product.getSoldAmount() + addProductToOrder.getQuantity());
        product.setTotalInStock(product.getTotalInStock() - addProductToOrder.getQuantity());
        productRepository.save(product);

    }
    public Double handleVoucher(Long voucherId,Order order,Double totalPrice)
    {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if (voucher.getAmount() != null && !voucher.getAmount().equals(Integer.valueOf(0)))
        {
            order.setVoucherId(voucherId);
            voucher.setAmount(voucher.getAmount()-1);
            voucherRepository.save(voucher);
        }
        totalPrice = totalPrice - (totalPrice*voucher.getPercent())/100;
        return totalPrice;
    }


}

