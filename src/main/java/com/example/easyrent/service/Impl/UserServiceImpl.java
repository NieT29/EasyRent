package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.User;
import com.example.easyrent.exception.BadRequestException;
import com.example.easyrent.model.request.ChangePasswordRequest;
import com.example.easyrent.model.request.UpdateProfileUserRequest;
import com.example.easyrent.repository.UserRepository;
import com.example.easyrent.security.CustomUserDetails;
import com.example.easyrent.security.SecurityUtils;
import com.example.easyrent.service.FileServerService;
import com.example.easyrent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileServerService fileServerService;

    @Override
    public void updateProfileUser(UpdateProfileUserRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        Optional<User> existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Số điện thoại đã được đăng ký");
        }

        currentUser.setName(request.getName());
        currentUser.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(currentUser);
    }

    @Override
    public String uploadAvatar(MultipartFile avatar) {
        User currentUser = SecurityUtils.getCurrentUser();

        // Lưu file avatar và trả về URL
        String avatarUrl = fileServerService.saveAvatarUser(avatar, currentUser.getId());

        // Nếu người dùng đã có avatar cũ, xóa avatar cũ
        if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
            if (currentUser.getAvatar().startsWith("/" + FileServerService.imageUploadDir + "/")) {
                fileServerService.deleteFile(currentUser.getAvatar());
            }
        }

        // Cập nhật avatar mới vào đối tượng người dùng
        currentUser.setAvatar(avatarUrl);
        userRepository.save(currentUser);

        return avatarUrl;
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

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
