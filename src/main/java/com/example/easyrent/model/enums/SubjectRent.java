package com.example.easyrent.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubjectRent {
    MALE("Nam"),
    FAMALE("Nữ"),
    ALL("Tất cả");
    private final String value;
}