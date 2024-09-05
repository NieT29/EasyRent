package com.example.easyrent.controller.rest;

import com.example.easyrent.model.enums.PriceType;
import com.example.easyrent.model.response.ServiceTypeResponse;
import com.example.easyrent.service.ServicePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service-price")
public class ServicePriceApi {
    private final ServicePriceService servicePriceService;

    @GetMapping("/get-price-by-type")
    public ResponseEntity<List<ServiceTypeResponse>> getServicePriceByType(@RequestParam("priceType") PriceType priceType) {
        List<ServiceTypeResponse> serviceTypes = servicePriceService.getServiceByPriceType(priceType);

        return ResponseEntity.ok(serviceTypes);
    }
}
