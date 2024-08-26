package com.example.easyrent.service;

import com.example.easyrent.entity.User;

public interface MailService {
    void sendMailRegister(User user, String subject, String link);

    void sendMailForgetPassword(User user, String subject, String link);
}
