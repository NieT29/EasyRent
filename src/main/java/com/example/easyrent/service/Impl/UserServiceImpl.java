package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.User;
import com.example.easyrent.exception.BadRequestException;
import com.example.easyrent.model.request.ChangePasswordRequest;
import com.example.easyrent.repository.UserRepository;
import com.example.easyrent.security.CustomUserDetails;
import com.example.easyrent.security.CustomUserDetailsService;
import com.example.easyrent.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            throw new BadRequestException("Người dùng chưa đăng nhập");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        if (currentUser == null) {
            throw new BadRequestException("Không thể xác định người dùng");
        }

        return currentUser;
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User currentUser = getUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new BadRequestException("Mật khẩu hiện tại không đúng");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu nhập lại không khớp");
        }

        if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())) {
            throw new BadRequestException("Mật khẩu mới không không được trùng với mật khẩu hiện tại");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
        System.out.println("Password changed successfully for user: " + currentUser.getEmail());

    }


}
