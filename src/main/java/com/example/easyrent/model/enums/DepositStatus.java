package com.example.easyrent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DepositStatus {
    PENDING("Đang xử lý"),    // Giao dịch đang chờ xử lý
    COMPLETED("Thành công"),  // Giao dịch đã hoàn tất
    FAILED("Thất bại"),     // Giao dịch thất bại
    CANCELLED("Bị hủy");   // Giao dịch bị hủy

    private final String value;
}
