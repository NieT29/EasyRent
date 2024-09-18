package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.OrderService;
import com.example.easyrent.entity.ServicePrice;
import com.example.easyrent.entity.ServiceType;
import com.example.easyrent.exception.ResourceNotFoundException;
import com.example.easyrent.model.enums.OrderServiceStatus;
import com.example.easyrent.model.enums.PaymentStatus;
import com.example.easyrent.model.enums.TransactionType;
import com.example.easyrent.model.request.UpsertOrderServiceRequest;
import com.example.easyrent.repository.OrderServiceRepository;
import com.example.easyrent.repository.ServicePriceRepository;
import com.example.easyrent.repository.ServiceTypeRepository;
import com.example.easyrent.service.OrderServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderServiceServiceImpl implements OrderServiceService {
    private final OrderServiceRepository orderServiceRepository;
    private final ServicePriceRepository servicePriceRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    @Transactional
    @Override
    public OrderService updateOrderService(UpsertOrderServiceRequest request) {
        OrderService orderService = orderServiceRepository.findByRoomId(request.getRoomId());

        if (orderService == null) {
            throw new ResourceNotFoundException("Không tìm thấy orderService");
        }

        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServiceType"));
        ServicePrice servicePrice = servicePriceRepository.findById(request.getServicePriceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServicePrice"));

        orderService.setTransactionType(TransactionType.NEW_POST);
        orderService.setTotalDay(request.getTotalDay());
        orderService.setTotalPrice(request.getTotalPrice());
        orderService.setStatus(OrderServiceStatus.PENDING_PAYMENT);
        orderService.setPaymentStatus(PaymentStatus.UNPAID);
        orderService.setPaymentMethod(request.getPaymentMethod());
        orderService.setServiceType(serviceType);
        orderService.setServicePrice(servicePrice);
        orderServiceRepository.save(orderService);
        return orderService;
    }
}
