package com.example.easyrent.service;

import com.example.easyrent.entity.User;

public interface MailService {
    void sendMail1(User user, String subject, String link);

    void sendMail2(User user, String subject, String link);
}
