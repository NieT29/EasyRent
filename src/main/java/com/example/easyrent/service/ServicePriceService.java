package com.example.easyrent.service;

import com.example.easyrent.model.enums.PriceType;
import com.example.easyrent.model.response.ServiceTypeResponse;

import java.util.List;

public interface ServicePriceService {
    List<ServiceTypeResponse> getServiceByPriceType(PriceType priceType);
}
