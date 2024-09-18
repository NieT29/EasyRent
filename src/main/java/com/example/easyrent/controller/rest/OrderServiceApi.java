package com.example.easyrent.controller.rest;

import com.example.easyrent.entity.OrderService;
import com.example.easyrent.model.request.UpsertOrderServiceRequest;
import com.example.easyrent.model.response.OrderServiceIdResponse;
import com.example.easyrent.service.OrderServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order-service")
public class OrderServiceApi {
    private final OrderServiceService orderServiceService;

    @PutMapping("/update")
    public ResponseEntity<?> updateOrderService(@Valid @RequestBody UpsertOrderServiceRequest request) {
        OrderService orderService = orderServiceService.updateOrderService(request);
        OrderServiceIdResponse response = new OrderServiceIdResponse(orderService.getId());
        return ResponseEntity.ok(response);
    }
}
