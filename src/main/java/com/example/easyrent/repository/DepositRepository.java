package com.example.easyrent.repository;

import com.example.easyrent.entity.Deposit;
import com.example.easyrent.model.enums.DepositStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    Optional<Deposit> findByVnpTransactionId(String vnpTransactionId);

    List<Deposit> findByStatusAndCreatedAtBefore(DepositStatus depositStatus, LocalDateTime localDateTime);
}
