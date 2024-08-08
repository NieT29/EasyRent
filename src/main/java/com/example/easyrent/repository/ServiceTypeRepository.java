package com.example.easyrent.repository;

import com.example.easyrent.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
    ServiceType findByName(String name);
}
