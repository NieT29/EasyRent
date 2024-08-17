package com.example.easyrent.service;

import com.example.easyrent.model.request.LoginRequest;

public interface AuthService {
    void login(LoginRequest request);
}
