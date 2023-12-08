package com.techmarket.api.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ErrorCode;
import com.techmarket.api.form.transaction.CreatePaymentForm;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.User;
import com.techmarket.api.repository.OrderRepository;
import com.techmarket.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TransactionController extends ABasicController{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    APIContext apiContext;


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreatePaymentForm createPaymentForm, BindingResult bindingResult){

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Order order = orderRepository.findById(createPaymentForm.getOrderId()).orElse(null);
        if (order==null)
        {
            apiMessageDto.setMessage("Not found order");
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        try {
            Payment payment = createPayment(createPaymentForm);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                  apiMessageDto.setData(link.getHref());
                  return apiMessageDto;
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }

        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("Paypal is not available now, please contact to our customer service");
        return apiMessageDto;
    }
    @GetMapping("/deposit/cancel")
    public ApiMessageDto<String> cancelPay() {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("cancel");
        return apiMessageDto;
    }

    @GetMapping("/deposit/success")
    public ApiMessageDto<String> successPay(@RequestParam("paymentId") String paymentId,
                                                   @RequestParam("payerId") String payerId ,@RequestParam Long orderId) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Long accountId = getCurrentUser();

        User user = userRepository.findByAccountId(accountId).orElse(null);
        if (user==null)
        {
            apiMessageDto.setMessage("Not found user");
            apiMessageDto.setCode(ErrorCode.USER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        try {
            Payment payment = executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                Order order = orderRepository.findById(orderId).orElse(null);
                if (order==null)
                {
                    apiMessageDto.setMessage("order not found");
                    return apiMessageDto;
                }
                order.setIsPaid(true);
                orderRepository.save(order);


            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }


        return apiMessageDto;
    }
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
    public Payment createPayment(CreatePaymentForm createPaymentForm)throws PayPalRESTException
    {
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setName("Nạp tiền ");
        item.setCurrency("USD");
        item.setPrice(createPaymentForm.getAmount().toString());
        item.setQuantity("1");
        items.add(item);
        itemList.setItems(items);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(createPaymentForm.getAmount().toString());

        Transaction transaction = new Transaction();
        transaction.setDescription("Nạp tiền ");
        transaction.setAmount(amount);
        transaction.setItemList(itemList);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(createPaymentForm.getUrlSuccess());
        redirectUrls.setCancelUrl(createPaymentForm.getUrlCancel());
        payment.setRedirectUrls(redirectUrls);
        apiContext.setMaskRequestId(true);
        return payment.create(apiContext);
    }
}