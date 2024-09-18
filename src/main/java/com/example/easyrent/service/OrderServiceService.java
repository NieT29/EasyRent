package com.example.easyrent.service;

import com.example.easyrent.entity.OrderService;
import com.example.easyrent.model.request.UpsertOrderServiceRequest;

public interface OrderServiceService {
    OrderService updateOrderService(UpsertOrderServiceRequest request);
}
