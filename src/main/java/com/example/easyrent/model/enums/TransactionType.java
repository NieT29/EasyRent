package com.example.easyrent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    NEW_POST("Đăng tin mới"),          // Đăng tin mới
    UPGRADE_TO_VIP("Nâng cấp vip"),    // Nâng cấp lên tin VIP
    RENEW_POST("Gia hạn tin");       // Gia hạn tin

    private final String value;
}
