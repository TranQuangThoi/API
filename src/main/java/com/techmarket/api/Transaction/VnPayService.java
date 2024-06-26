package com.techmarket.api.Transaction;

import com.techmarket.api.config.VnPayConfig;
import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.dto.ApiResponse;
import com.techmarket.api.dto.paymentMethod.VNPAYDto;
import com.techmarket.api.form.transaction.CreatePaymentForm;
import com.techmarket.api.model.Order;
import com.techmarket.api.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VnPayService {
    private final VnPayConfig vnPayConfig;
    public ApiMessageDto<VNPAYDto> createVnPayPayment(HttpServletRequest request , CreatePaymentForm createPaymentForm, Order order) {
        System.out.println(order.getTotalMoney());
        long amount = (long) (order.getTotalMoney()*1L);
        System.out.println("gia tien:  "+ amount);
        String bankCode = "NCB";
//       String returnUrl = createPaymentForm.getUrlSuccess();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(order.getId(),createPaymentForm.getUrlSuccess(), createPaymentForm.getUrlCancel());
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        ApiMessageDto<VNPAYDto> apiResponse = new ApiMessageDto<>();
        VNPAYDto vnpayDto = new VNPAYDto();
        vnpayDto.setCode("200");
        vnpayDto.setPaymentUrl(paymentUrl);

        apiResponse.setData(vnpayDto);
        apiResponse.setMessage("success");

        return apiResponse;

    }
}
