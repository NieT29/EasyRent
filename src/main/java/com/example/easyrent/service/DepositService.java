package com.example.easyrent.service;

import com.example.easyrent.entity.Deposit;
import com.example.easyrent.entity.User;

import java.util.List;

public interface DepositService {
    Deposit createDeposit(User user, int amount, String vnpTxnRef, String paymentMethod);

    List<Deposit> getDepositHistory();
}
