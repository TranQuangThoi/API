package com.techmarket.api.service;

import com.techmarket.api.dto.cart.CartDto;
import com.techmarket.api.form.order.AddProductToOrder;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.OrderDetail;
import com.techmarket.api.model.ProductVariant;
import com.techmarket.api.repository.OrderDetailRepository;
import com.techmarket.api.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String email, String msg, String subject, boolean html) {
        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(msg,html);

            emailSender.send(message);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
    public void sendOtpToEmail(String fullName, String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");

        String emailContent = "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
                "            background-color: #e9eff1;" +
                "            color: #4a4a4a;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            box-sizing: border-box;" +
                "            border: 1px solid #e7e7e7;" +
                "            border-radius: 5px;" +
                "        }" +
                "       .fullName {" +
                "             font-size: 25px; " +
                "             color: #007bff; " +
                "            margin-bottom: 15px;" +
                "          }"+
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            background: #b8f1b7;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);" +
                "            padding: 30px;" +
                "            text-align: center;" +
                "        }" +
                "        h1 {" +
                "            color: #007bff;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 20px;" +
                "            margin-top: 5px;" +
                "        }" +
                "        p {" +
                "            font-size: 16px;" +
                "            line-height: 1.5;" +
                "            color: #666;" +
                "        }" +
                "        .otp {" +
                "            display: inline-block;" +
                "            margin: 20px auto;" +
                "            padding: 10px 20px;" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: #007bff;" +
                "            background-color: #f0f8ff;" +
                "            border: 1px solid #b6dfff;" +
                "            border-radius: 5px;" +
                "        }" +
                "        .footer {" +
                "             text-align: center;" +
                "             padding: 20px;" +
                "             font-size: 14px;" +
                "             color: #777;" +
                "             background-color: #f8f8f8;" +
                "             border-top: 1px solid #e7e7e7;" +
                "              }" +
                "        .footer a {" +
                "              color: #007bff;" +
                "              text-decoration: none;" +
                "               }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"fullName\">Hello %s</div>" +
                "        <h1>OTP Verification</h1>" +
                "        <p>Thank you for registering with us. Your One Time Password (OTP) is:</p>" +
                "        <div class=\"otp\">%s</div>" +
                "        <p>Please use this OTP to complete your account verification.</p>" +
                "        <p><strong>Thank you for choosing us!</strong></p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "          <p>Need help? Contact us at <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                "           <p>&copy; 2023 Teck Market. All rights reserved.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setText(String.format(emailContent, fullName, otp), true);

        emailSender.send(mimeMessage);
    }
    public void sendOtpToEmailForResetPass(String fullName, String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");

        String emailContent = "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
                "            background-color: #e9eff1;" +
                "            color: #4a4a4a;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            box-sizing: border-box;" +
                "            border: 1px solid #e7e7e7;" +
                "            border-radius: 5px;" +
                "        }" +
                "       .fullName {" +
                "             font-size: 25px; " +
                "             color: #007bff; " +
                "            margin-bottom: 15px;" +
                "          }"+
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            background: #b8f1b7;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);" +
                "            padding: 30px;" +
                "            text-align: center;" +
                "        }" +
                "        h1 {" +
                "            color: #007bff;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 20px;" +
                "            margin-top: 5px;" +
                "        }" +
                "        p {" +
                "            font-size: 16px;" +
                "            line-height: 1.5;" +
                "            color: #666;" +
                "        }" +
                "        .otp {" +
                "            display: inline-block;" +
                "            margin: 20px auto;" +
                "            padding: 10px 20px;" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: #007bff;" +
                "            background-color: #f0f8ff;" +
                "            border: 1px solid #b6dfff;" +
                "            border-radius: 5px;" +
                "        }" +
                "        .footer {" +
                "             text-align: center;" +
                "             padding: 20px;" +
                "             font-size: 14px;" +
                "             color: #777;" +
                "             background-color: #f8f8f8;" +
                "             border-top: 1px solid #e7e7e7;" +
                "              }" +
                "        .footer a {" +
                "              color: #007bff;" +
                "              text-decoration: none;" +
                "               }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"fullName\">Hello %s</div>" +
                "        <h1>OTP Verification</h1>" +
                "        <p>Your One Time Password (OTP) is:</p>" +
                "        <div class=\"otp\">%s</div>" +
                "        <p>Please use this OTP to complete your account verification.</p>" +
                "        <p><strong>Thank you for choosing us!</strong></p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "          <p>Need help? Contact us at <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                "           <p>&copy; 2023 Teck Market. All rights reserved.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setText(String.format(emailContent, fullName, otp), true);

        emailSender.send(mimeMessage);
    }
    public void sendOrderToEmail(List<AddProductToOrder> cartDtos, Order order, String email) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("You have placed your order successfully");

        String orderContent = "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .container {" +
                "            max-width: 1000px;" +
                "            margin: 30px auto;" +
                "            background: whitesmoke;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "            padding: 30px;" +
                "            text-align: center;" +
                "        }" +
                "        .fullName {" +
                "            font-size: 25px;" +
                "            color: #007bff;" +
                "            margin-bottom: 15px;" +
                "        }" +
                "        h1 {" +
                "            color: #007bff;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 20px;" +
                "            margin-top: 5px;" +
                "        }" +
                "        p {" +
                "            font-size: 16px;" +
                "            line-height: 1.5;" +
                "            color: #666;" +
                "        }" +
                "        ul {" +
                "            list-style: none;" +
                "            padding: 0;" +
                "        }" +
                "        li {" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        .otp {" +
                "            display: inline-block;" +
                "            margin: 20px auto;" +
                "            padding: 10px 20px;" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: #007bff;" +
                "            background-color: #f0f8ff;" +
                "            border: 1px solid #b6dfff;" +
                "            border-radius: 5px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            font-size: 14px;" +
                "            color: #777;" +
                "            background-color: #f8f8f8;" +
                "            border-top: 1px solid #e7e7e7;" +
                "        }" +
                "        .footer a {" +
                "            color: #007bff;" +
                "            text-decoration: none;" +
                "        }" +
                "  table {" +
                "            width: 100%;" +
                "            border-collapse: collapse;" +
                "        }" +
                "        th, td {" +
                "            border: 1px solid #ddd;" +
                "            padding: 8px;" +
                "        }"+
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "    <h1>The information about the order</h1>" +
                "    <p>Your order with the following details has been confirmed:</p>" +
                "    <p><strong>Order ID:</strong> " + order.getOrderCode() + "</p>" +
                "    <table border=\"1\" style=\"width: 100%; border-collapse: collapse;\">" +
                "        <tr>" +
                "            <th>Product</th>" +
                "            <th>Quantity</th>" +
                "            <th>Color</th>" +
                "            <th>Price</th>" +
                "        </tr>";

// Thêm danh sách sản phẩm vào emailContent
        for (AddProductToOrder item : cartDtos) {
            orderContent += String.format(
                    "<tr>" +
                            "    <td>%s</td>" +
                            "    <td>%d</td>" +
                            "    <td>%s</td>" +
                            "    <td>%.2f đ</td>" +
                            "</tr>"
                    ,
                    item.getProductName(),
                    item.getQuantity(),
                    item.getColor(),
                    item.getPrice()
            );
        }

        orderContent += String.format(
                "        </table>" +
                        "        <p><strong>PaymentMethod:</strong> COD</p>" +
                        "        <p><strong>Total Amount:</strong> %.2f đ</p>" +
                        "        <p><strong>Thank you for choosing us!</strong></p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "          <p>Need help? Contact us at <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                        "           <p>&copy; 2023 Teck Market. All rights reserved.</p>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                 order.getTotalMoney());

        mimeMessageHelper.setText(orderContent, true);

        emailSender.send(mimeMessage);
    }
    public void sendOrderPaidToEmail(Order order, String email) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("You have placed your order successfully");
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getId());
        String payment ;
        if (!order.getIsPaid())
        {
            payment = "Unpaid";
        }else {
            payment = "Paid";
        }

        String orderContent = "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .container {" +
                "            max-width: 1000px;" +
                "            margin: 30px auto;" +
                "            background: whitesmoke;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "            padding: 30px;" +
                "            text-align: center;" +
                "        }" +
                "        .fullName {" +
                "            font-size: 25px;" +
                "            color: #007bff;" +
                "            margin-bottom: 15px;" +
                "        }" +
                "        h1 {" +
                "            color: #007bff;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 20px;" +
                "            margin-top: 5px;" +
                "        }" +
                "        p {" +
                "            font-size: 16px;" +
                "            line-height: 1.5;" +
                "            color: #666;" +
                "        }" +
                "        ul {" +
                "            list-style: none;" +
                "            padding: 0;" +
                "        }" +
                "        li {" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        .otp {" +
                "            display: inline-block;" +
                "            margin: 20px auto;" +
                "            padding: 10px 20px;" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: #007bff;" +
                "            background-color: #f0f8ff;" +
                "            border: 1px solid #b6dfff;" +
                "            border-radius: 5px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            font-size: 14px;" +
                "            color: #777;" +
                "            background-color: #f8f8f8;" +
                "            border-top: 1px solid #e7e7e7;" +
                "        }" +
                "        .footer a {" +
                "            color: #007bff;" +
                "            text-decoration: none;" +
                "        }" +
                "  table {" +
                "            width: 100%;" +
                "            border-collapse: collapse;" +
                "        }" +
                "        th, td {" +
                "            border: 1px solid #ddd;" +
                "            padding: 8px;" +
                "        }"+
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "    <h1>The information about the order</h1>" +
                "    <p>Your order with the following details has been confirmed:</p>" +
                "    <p><strong>Order ID:</strong> " + order.getOrderCode() + "</p>" +
                "    <table border=\"1\" style=\"width: 100%; border-collapse: collapse;\">" +
                "        <tr>" +
                "            <th>Product</th>" +
                "            <th>Quantity</th>" +
                "            <th>Color</th>" +
                "            <th>Price</th>" +
                "        </tr>";

// Thêm danh sách sản phẩm vào emailContent
        for (OrderDetail item : orderDetails) {
            orderContent += String.format(
                    "<tr>" +
                            "    <td>%s</td>" +
                            "    <td>%d</td>" +
                            "    <td>%s</td>" +
                            "    <td>%.2f đ</td>" +
                            "</tr>",
                    item.getName(),
                    item.getAmount(),
                    item.getColor(),
                    item.getPrice()
            );
        }

        orderContent += String.format(
                "        </table>" +
                        "        <p><strong>PaymentMethod:</strong> Paypal</p>" +
                        "        <p><strong>Total Amount:</strong> %.2f đ</p>" +
                        "        <p><strong>Payment status:</strong>" + payment+ "</p>" +
                        "        <p><strong>Thank you for choosing us!</strong></p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "          <p>Need help? Contact us at <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                        "           <p>&copy; 2023 Teck Market. All rights reserved.</p>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                order.getTotalMoney());

        mimeMessageHelper.setText(orderContent, true);

        emailSender.send(mimeMessage);
    }


}
