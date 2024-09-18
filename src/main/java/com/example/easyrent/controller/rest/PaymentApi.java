package com.example.easyrent.controller.rest;

import com.example.easyrent.model.request.DepositRequest;
import com.example.easyrent.model.request.PaymentOrderServiceRequest;
import com.example.easyrent.model.response.PaymentResponse;
import com.example.easyrent.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentApi {
    private final PaymentService paymentService;

    @PostMapping("/create_payment")
    public ResponseEntity<?> createDeposit(@Valid @RequestBody DepositRequest depositRequest, HttpServletRequest request) {
        PaymentResponse paymentResponse = paymentService.createPaymentResponse(depositRequest, request);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/payment-from-account")
    public ResponseEntity<?> paymentFromAccount(@Valid @RequestBody PaymentOrderServiceRequest request) {
        String paymentResult = paymentService.paymentFromAccount(request.getOrderServiceId());
        return ResponseEntity.ok(paymentResult);
    }
}
