package com.example.easyrent.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    private String status;
    private String message;
    private String transactionId;
    private String paymentUrl;
}
