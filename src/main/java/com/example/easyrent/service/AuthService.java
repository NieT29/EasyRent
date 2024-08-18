package com.example.easyrent.service;

import com.example.easyrent.entity.User;
import com.example.easyrent.model.request.ForgetPasswordRequest;
import com.example.easyrent.model.request.LoginRequest;
import com.example.easyrent.model.request.RegisterRequest;
import com.example.easyrent.model.request.ResetPasswordRequest;
import com.example.easyrent.model.response.VerifyResponse;

public interface AuthService {
    void login(LoginRequest request);

    void register(RegisterRequest request);

    VerifyResponse confirmRegistration(String token);

    VerifyResponse confirmResetPassword(String token);

    void resetPassword(ResetPasswordRequest request);

    void forgetPassword(ForgetPasswordRequest request);
}
