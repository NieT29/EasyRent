package com.example.easyrent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PriceType {
    DAILY("ngày"),
    WEEKLY("tuần"),
    MONTHLY("tháng");

    private final String value;
}
