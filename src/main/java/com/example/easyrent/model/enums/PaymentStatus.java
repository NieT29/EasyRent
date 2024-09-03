package com.example.easyrent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatus {
    PENDING("Đang xử lý"),
    COMPLETED("Thành công"),
    FAILED("Thất bại"),
    UNPAID("Chưa thanh toán");

    private final String value;
}
