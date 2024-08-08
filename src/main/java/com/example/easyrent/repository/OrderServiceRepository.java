package com.example.easyrent.repository;

import com.example.easyrent.entity.OrderService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderServiceRepository extends JpaRepository<OrderService, Integer> {
}
