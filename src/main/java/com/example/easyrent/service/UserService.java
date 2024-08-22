package com.example.easyrent.service;

import com.example.easyrent.model.request.ChangePasswordRequest;

public interface UserService {
    void changePassword(ChangePasswordRequest request);
}
