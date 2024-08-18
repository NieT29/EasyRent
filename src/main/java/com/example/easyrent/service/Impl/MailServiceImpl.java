package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.User;
import com.example.easyrent.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender sender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendMail1(User user, String subject, String link) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Set encoding to UTF-8
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            final Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("link", link);

            // Create the HTML body using Thymeleaf
            final String htmlContent = templateEngine.process("mail/mail-register.html", context);
            helper.setText(htmlContent, true);

            // Send Message!
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMail2(User user, String subject, String link) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Set encoding to UTF-8
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            final Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("link", link);

            // Create the HTML body using Thymeleaf
            final String htmlContent = templateEngine.process("mail/mail-forget-password.html", context);
            helper.setText(htmlContent, true);

            // Send Message!
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
