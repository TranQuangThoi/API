package com.techmarket.api.Transaction;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.techmarket.api.form.transaction.CreatePaymentForm;
import com.techmarket.api.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    APIContext apiContext;

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
    private String convertToUSD(Double amount){
        //Exchange rate from USD  to VND: 1 USD = 24230 VND (20/12/2023)
        double convertCurrency = amount / 24230;
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Định dạng số thập phân với hai chữ số sau dấu thập phân
        return decimalFormat.format(convertCurrency);
    }
    public Payment createPayment(CreatePaymentForm createPaymentForm, Order order)throws PayPalRESTException
    {
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setName("Nạp tiền ");
        item.setCurrency("USD");
        item.setPrice(convertToUSD(order.getTotalMoney()));
        item.setQuantity("1");
        items.add(item);
        itemList.setItems(items);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(convertToUSD(order.getTotalMoney()));

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
