package com.example.easyrent.schedule;

import com.example.easyrent.entity.Deposit;
import com.example.easyrent.model.enums.DepositStatus;
import com.example.easyrent.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DepositScheduleTask {
    private final DepositRepository depositRepository;

    @Transactional
    @Scheduled(fixedRate = 60000) // Chạy mỗi phút
    public void checkPendingDeposit() {
        LocalDateTime now = LocalDateTime.now();
        List<Deposit> pendingDeposits = depositRepository.findByStatusAndCreatedAtBefore(DepositStatus.PENDING, now.minusMinutes(2));

        for (Deposit deposit : pendingDeposits) {
            deposit.setStatus(DepositStatus.FAILED);
            depositRepository.save(deposit);
        }
    }
}
