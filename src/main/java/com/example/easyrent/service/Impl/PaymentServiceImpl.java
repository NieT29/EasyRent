package com.example.easyrent.service.Impl;

import com.example.easyrent.config.VNPayConfig;
import com.example.easyrent.entity.Deposit;
import com.example.easyrent.entity.User;
import com.example.easyrent.exception.BadRequestException;
import com.example.easyrent.model.request.DepositRequest;
import com.example.easyrent.model.response.PaymentResponse;
import com.example.easyrent.security.CustomUserDetails;
import com.example.easyrent.service.DepositService;
import com.example.easyrent.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final DepositService depositService;

    @Override
    public PaymentResponse createPaymentResponse(DepositRequest depositRequest, HttpServletRequest request) {
        User currentUser = getUser(); // Lấy người dùng hiện tại

        int amount = depositRequest.getAmount();
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8); // Mã giao dịch duy nhất
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);

        // Sử dụng DepositService để tạo đối tượng Deposit
        Deposit deposit = depositService.createDeposit(currentUser, amount, vnp_TxnRef, "VNPay");

        long amountInCents = deposit.getAmount() * 100; // Số tiền chuyển đổi sang cents

        // Tạo các tham số cho VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInCents));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Nap tien vao tai khoan: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", VNPayConfig.orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 2);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Sắp xếp và mã hóa các tham số
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status("OK")
                .message("Successfully created payment link: " + vnp_TxnRef)
                .transactionId(vnp_TxnRef)
                .paymentUrl(paymentUrl)
                .build();

        return paymentResponse;
    }

    private static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            throw new BadRequestException("Người dùng chưa đăng nhập");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        if (currentUser == null) {
            throw new BadRequestException("Không thể xác định người dùng");
        }

        return currentUser;
    }
}
