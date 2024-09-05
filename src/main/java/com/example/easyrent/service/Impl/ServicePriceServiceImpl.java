package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.ServicePrice;
import com.example.easyrent.entity.ServiceType;
import com.example.easyrent.exception.BadRequestException;
import com.example.easyrent.model.enums.PriceType;
import com.example.easyrent.model.response.ServiceTypeResponse;
import com.example.easyrent.repository.ServicePriceRepository;
import com.example.easyrent.repository.ServiceTypeRepository;
import com.example.easyrent.service.ServicePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePriceServiceImpl implements ServicePriceService {
    private final ServicePriceRepository servicePriceRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<ServiceTypeResponse> getServiceByPriceType(PriceType priceType) {
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
        List<ServiceTypeResponse> responseList = new ArrayList<>();

        for (ServiceType serviceType : serviceTypes) {
            List<ServicePrice> servicePrices = servicePriceRepository.findByServiceTypeAndPriceType(serviceType, priceType);

            for (ServicePrice servicePrice : servicePrices) {
                ServiceTypeResponse response = new ServiceTypeResponse();
                response.setId(serviceType.getId());
                response.setName(serviceType.getName());
                response.setPrice(servicePrice.getDiscountPrice());
                response.setPriceType(priceType);
                responseList.add(response);
            }
        }
        return responseList;
    }

}
