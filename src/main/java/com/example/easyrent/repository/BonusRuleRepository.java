package com.example.easyrent.repository;

import com.example.easyrent.entity.BonusRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BonusRuleRepository extends JpaRepository<BonusRule, Integer> {
    Optional<BonusRule> findFirstByMinAmountLessThanEqualAndMaxAmountGreaterThan(int minAmount, int maxAmount);
}

