package com.example.easyrent.service;

import com.example.easyrent.model.request.DepositRequest;
import com.example.easyrent.model.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentResponse createPaymentResponse(DepositRequest depositRequest, HttpServletRequest request);
}
