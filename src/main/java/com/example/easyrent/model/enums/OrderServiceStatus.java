package com.example.easyrent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderServiceStatus {
    PENDING_APPROVAL, // Đang chờ duyệt
    ACTIVE,           // Đang hiển thị
    EXPIRED,          // Đã hết hạn
    CANCELED           // Đã bị hủy
}
