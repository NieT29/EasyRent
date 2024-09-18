package com.example.easyrent.model.request;

import com.example.easyrent.entity.ServicePrice;
import com.example.easyrent.entity.ServiceType;
import com.example.easyrent.model.enums.PaymentMethod;
import com.example.easyrent.model.enums.PriceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertOrderServiceRequest {
    @Min(value = 1, message = "Số ngày đăng tin phải lớn hơn 0")
    int totalDay;

    @Min(value = 1, message = "Tổng giá trị phải lớn hơn 0")
    int totalPrice;

    @NotNull(message = "Loại dịch vụ không được để trống")
    Integer serviceTypeId;

    @NotNull(message = "Giá dịch vụ không được để trống")
    Integer servicePriceId;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    PaymentMethod paymentMethod;

    PriceType priceType;

    Integer roomId;
}
