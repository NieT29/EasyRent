package com.example.easyrent.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceTypeAttributesDTO {
    String name;
    int priority;
    boolean showPhone;
    String titleColor;
}
