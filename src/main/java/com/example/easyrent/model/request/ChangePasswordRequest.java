package com.example.easyrent.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotEmpty(message = "Mật khẩu cũ không được để trống")
    String currentPassword;

    @NotEmpty(message = "Mật khẩu không được để trống")
    String newPassword;

    @NotEmpty(message = "Mật khẩu nhập lại không được để trống")
    String confirmPassword;
}
