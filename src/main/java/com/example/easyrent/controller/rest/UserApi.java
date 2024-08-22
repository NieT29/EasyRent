package com.example.easyrent.controller.rest;

import com.example.easyrent.model.request.ChangePasswordRequest;
import com.example.easyrent.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApi {
    private final UserService userService;

    @PutMapping("/change-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }
}
