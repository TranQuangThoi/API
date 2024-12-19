package com.techmarket.api.service;


import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.controller.ABasicController;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.form.order.AddProductToOrder;
import com.techmarket.api.form.order.CreateOrderForUser;
import com.techmarket.api.form.order.CreateOrderForm;
import com.techmarket.api.model.*;
import com.techmarket.api.repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import java.util.ArrayList;
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

    public void createOrder(CreateOrderForm createOrderForm , Order order) throws MessagingException {
        Double totalPrice=0.0;
        Double totalOriganal =0.0;
        Double totalPriceSale =0.0;
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
                totalPriceSale += totalPrice1Pro;

            }else {
                totalPrice += productVariantCheck.getPrice()* item.getQuantity();
                totalPrice1Pro += productVariantCheck.getPrice()* item.getQuantity();
            }
            handleOrder(productVariantCheck,item,orderDetail,totalPrice1Pro);
            item.setPrice(totalPrice);
            totalOriganal += productVariantCheck.getPrice()*item.getQuantity();

        }
        if (createOrderForm.getVoucherId()!=null)
        {
           handleVoucher(createOrderForm.getVoucherId(),order,totalPrice);
        }
        order.setOriginalTotal(totalOriganal);
        order.setTotalPriceSaleOff(totalPriceSale);
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

            Long accountId = getCurrentUser();
            User user = userRepository.findByAccountId(accountId).orElse(null);


        Double totalPrice=0.0;
        Double totalOriganal =0.0;
        Double totalPriceSale =0.0;

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
                totalPriceSale += ((productVariantCheck.getPrice()*product.getSaleOff())/100)*item.getQuantity();

            }else {
                totalPrice += productVariantCheck.getPrice()* item.getQuantity();
                totalPrice1Pro += productVariantCheck.getPrice()* item.getQuantity();

            }
            handleOrder(productVariantCheck,item,orderDetail,totalPrice1Pro);
            totalOriganal += productVariantCheck.getPrice()*item.getQuantity();


        }
        order.setOriginalTotal(totalOriganal);
        order.setTotalPriceSaleOff(totalPriceSale);
        Double totalPriceAfter = 0.0;
        if (createOrderForm.getVoucherId()!=null)
        {
             totalPriceAfter = handleVoucher(createOrderForm.getVoucherId(),order,totalPrice);
            Voucher voucher = voucherRepository.findById(createOrderForm.getVoucherId()).orElse(null);
            List<User> users  = new ArrayList<>();
            users.add(user);
            voucher.setIs_used(users);
            voucherRepository.save(voucher);
            order.setTotalMoney(totalPriceAfter);
        }else{
            order.setTotalMoney(totalPrice);
        }

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
            Double totalDiscount = ((double)voucher.getPercent()*totalPrice)/100;

            if(totalDiscount > voucher.getPriceMax())
            {
                totalDiscount = (double)voucher.getPriceMax();

            }

            order.setVoucherId(voucherId);
            order.setTotalPriceVoucher(totalDiscount);
            orderRepository.save(order);
            voucher.setAmount(voucher.getAmount()-1);
            voucherRepository.save(voucher);
            totalPrice = totalPrice - totalDiscount;
        }
        return totalPrice;
    }


}

