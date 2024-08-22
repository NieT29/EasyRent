package com.example.easyrent.controller.rest;

import com.example.easyrent.model.request.ChangePasswordRequest;
import com.example.easyrent.model.request.UpdateProfileUserRequest;
import com.example.easyrent.service.FileServerService;
import com.example.easyrent.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApi {
    private final UserService userService;
    private final FileServerService fileServerService;

    @PutMapping("/change-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileUserRequest request){
        userService.updateProfileUser(request);
        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {

        String avatarUrl = userService.uploadAvatar(avatar);
        return ResponseEntity.ok(avatarUrl);
    }


}
