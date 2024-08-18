package com.example.easyrent.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotEmpty(message = "Token không được để trống")
    String tokenString;

    @NotEmpty(message = "Mật khẩu không được để trống")
    String password;

    @NotEmpty(message = "Mật khẩu nhập lại không được để trống")
    String confirmPassword;
}
