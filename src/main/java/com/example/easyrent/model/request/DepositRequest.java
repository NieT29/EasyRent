package com.example.easyrent.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepositRequest {
    @NotNull(message = "Số tiền không được để trống")
    @Min(value = 10000, message = "Bạn phải nạp tối thiểu là 10.000đ")
    @Max(value = 5000000, message = "Số tiền nạp tối đa là 5.000.000đ")
    int amount;
}
