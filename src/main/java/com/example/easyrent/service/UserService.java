package com.example.easyrent.service;

import com.example.easyrent.model.request.ChangePasswordRequest;
import com.example.easyrent.model.request.UpdateProfileUserRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void changePassword(ChangePasswordRequest request);

    void updateProfileUser(UpdateProfileUserRequest request);

    String uploadAvatar(MultipartFile avatar);
}
