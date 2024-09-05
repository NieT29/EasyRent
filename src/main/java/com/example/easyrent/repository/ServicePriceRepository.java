package com.example.easyrent.repository;

import com.example.easyrent.entity.ServicePrice;
import com.example.easyrent.entity.ServiceType;
import com.example.easyrent.model.enums.PriceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicePriceRepository extends JpaRepository<ServicePrice, Integer> {

    List<ServicePrice> findByServiceTypeAndPriceType(ServiceType serviceType, PriceType priceType);
}
