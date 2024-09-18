package com.example.easyrent.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentOrderServiceRequest {
    @NotNull(message = "OrderService ID không được để trống.")
    Integer orderServiceId;
}
