package com.example.easyrent.repository;

import com.example.easyrent.entity.DepositHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Integer> {
}
