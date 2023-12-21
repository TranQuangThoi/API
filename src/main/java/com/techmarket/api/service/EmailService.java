package com.techmarket.api.service;

import com.techmarket.api.form.order.AddProductToOrder;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.OrderDetail;
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
import java.util.Locale;
import java.util.List;
import java.text.NumberFormat;

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
        mimeMessageHelper.setSubject("Mã xác nhận");

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
                "        <h1>Mã xác nhận</h1>" +
                "        <p>Cảm ơn vì đăng ký với chúng tôi.Mã xác nhận (OTP) của bạn là:</p>" +
                "        <div class=\"otp\">%s</div>" +
                "        <p>Vui lòng sử dụng OTP này để hoàn tất xác minh tài khoản của bạn.</p>" +
                "        <p><strong>Cảm ơn bạn đã lựa chọn chúng tôi!</strong></p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "          <p>Nếu cân giúp đỡ? Hãy liên hệ tại <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                "           <p>&copy; 2023 Teck Market.</p>" +
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
        mimeMessageHelper.setSubject("Đặt lại mật khẩu");

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
                "        <h1>Mã xác nhận</h1>" +
                "        <div class=\"otp\">%s</div>" +
                "        <p>Vui lòng sử dụng OTP này để hoàn tất xác minh tài khoản của bạn.</p>" +
                "        <p><strong>Cảm ơn bạn đã tin tưởng và lựa chọn chúng tôi!</strong></p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "          <p>Nếu cân giúp đỡ? Hãy liên hệ tại <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                "           <p>&copy; 2023 Teck Market.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setText(String.format(emailContent, fullName, otp), true);

        emailSender.send(mimeMessage);
    }
    public void sendOrderToEmail(List<AddProductToOrder> cartDtos, Order order, String email) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        Locale.setDefault(new Locale("vi", "VN"));

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Bạn đã đặt hàng thành công");

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
                "    <h1>Thông tin đơn hàng</h1>" +
                "    <p>Đơn đặt hàng của bạn với các chi tiết sau đã được xác nhận:</p>" +
                "    <p><strong>Mã đơn hàng:</strong> " + order.getOrderCode() + "</p>" +
                "    <table border=\"1\" style=\"width: 100%; border-collapse: collapse;\">" +
                "        <tr>" +
                "            <th>Sản phẩm</th>" +
                "            <th>Số lượng</th>" +
                "            <th>Màu</th>" +
                "            <th>Giá</th>" +
                "        </tr>";

// Thêm danh sách sản phẩm vào emailContent
        for (AddProductToOrder item : cartDtos) {
            String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(item.getPrice());
            orderContent += String.format(
                    "<tr>" +
                            "    <td>%s</td>" +
                            "    <td>%d</td>" +
                            "    <td>%s</td>" +
                            "    <td>%s</td>" +
                            "</tr>"
                    ,
                    item.getProductName(),
                    item.getQuantity(),
                    item.getColor(),
                   formattedPrice
            );
        }
        String formatTotal = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(order.getTotalMoney());
        orderContent += String.format(
                "        </table>" +
                        "        <p><strong>Phương thức thanh toán:</strong> COD</p>" +
                        "        <p><strong>Tổng tiền:</strong> %s</p>" +
                        "        <p><strong>Cảm ơn bạn đã tin tưởng và lựa chọn chúng tôi!</strong></p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "          <p>Nếu cân giúp đỡ? Hãy liên hệ tại <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                        "           <p>&copy; 2023 Teck Market.</p>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                 formatTotal);

        mimeMessageHelper.setText(orderContent, true);

        emailSender.send(mimeMessage);
    }
    public void sendOrderPaidToEmail(Order order, String email) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        Locale.setDefault(new Locale("vi", "VN"));
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Bạn đã đặt hàng thành công");
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getId());
        String payment ;
        if (!order.getIsPaid())
        {
            payment = "Chưa thanh toán";
        }else {
            payment = "Đã thanh toán";
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
                "    <h1>Thông tin đơn hàng</h1>" +
                "    <p>Đơn đặt hàng của bạn với các chi tiết sau đã được xác nhận:</p>" +
                "    <p><strong>Mã đơn hàng:</strong> " + order.getOrderCode() + "</p>" +
                "    <table border=\"1\" style=\"width: 100%; border-collapse: collapse;\">" +
                "        <tr>" +
                "            <th>Sản phẩm</th>" +
                "            <th>Số lượng</th>" +
                "            <th>Màu</th>" +
                "            <th>Giá</th>" +
                "        </tr>";

// Thêm danh sách sản phẩm vào emailContent
        for (OrderDetail item : orderDetails) {
            String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(item.getPrice());
            orderContent += String.format(
                    "<tr>" +
                            "    <td>%s</td>" +
                            "    <td>%d</td>" +
                            "    <td>%s</td>" +
                            "    <td>%s</td>" +
                            "</tr>",
                    item.getName(),
                    item.getAmount(),
                    item.getColor(),
                    formattedPrice
            );
        }
        String formatTotal = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(order.getTotalMoney());
        orderContent += String.format(
                "        </table>" +
                        "        <p><strong>Phương thức thanh toán:</strong> Paypal</p>" +
                        "        <p><strong>Tổng tiền:</strong> %s</p>" +
                        "        <p><strong>Trạng thái thanh toán:</strong>" + payment+ "</p>" +
                        "        <p><strong>Cảm ơn bạn đã tin tưởng và lựa chọn chúng tôi!</strong></p>" +
                        "    </div>" +
                        "    <div class=\"footer\">" +
                        "          <p>Nếu cân giúp đỡ? Hãy liên hệ tại  <a href=\"mailto:tranquangthoik20@gmail.com\">tranquangthoik20@gmail.com</a></p>" +
                        "           <p>&copy; 2023 Teck Market.</p>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                formatTotal);

        mimeMessageHelper.setText(orderContent, true);

        emailSender.send(mimeMessage);
    }


}
