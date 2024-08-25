package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.BonusRule;
import com.example.easyrent.entity.Deposit;
import com.example.easyrent.entity.User;
import com.example.easyrent.model.enums.DepositStatus;
import com.example.easyrent.repository.BonusRuleRepository;
import com.example.easyrent.repository.DepositRepository;
import com.example.easyrent.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final BonusRuleRepository bonusRuleRepository;


    @Override
    public Deposit createDeposit(User user, int amount, String vnpTxnRef, String paymentMethod) {
        BonusRule bonusRule = bonusRuleRepository.findFirstByMinAmountLessThanEqualAndMaxAmountGreaterThan(amount, amount)
                .orElse(null);

        int bonusAmount = 0;
        int totalAmount = amount;

        if (bonusRule != null) {
            bonusAmount = (amount * bonusRule.getBonusPercentage()) / 100;
            totalAmount += bonusAmount;
        }
        Deposit deposit = Deposit.builder()
                .user(user)
                .amount(amount)
                .bonusAmount(bonusAmount)
                .totalAmount(totalAmount)
                .status(DepositStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .bonusRule(bonusRule)
                .vnpTransactionId(vnpTxnRef)
                .paymentMethod(paymentMethod)
                .build();
        return depositRepository.save(deposit);
    }
}
