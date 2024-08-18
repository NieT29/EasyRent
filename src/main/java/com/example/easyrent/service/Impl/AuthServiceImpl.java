package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.TokenConfirm;
import com.example.easyrent.entity.User;
import com.example.easyrent.exception.BadRequestException;
import com.example.easyrent.model.enums.TokenType;
import com.example.easyrent.model.enums.UserRole;
import com.example.easyrent.model.request.ForgetPasswordRequest;
import com.example.easyrent.model.request.LoginRequest;
import com.example.easyrent.model.request.RegisterRequest;
import com.example.easyrent.model.request.ResetPasswordRequest;
import com.example.easyrent.model.response.VerifyResponse;
import com.example.easyrent.repository.TokenConfirmRepository;
import com.example.easyrent.repository.UserRepository;
import com.example.easyrent.service.AuthService;
import com.example.easyrent.service.MailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final AuthenticationManager authenticationManager;
    private final TokenConfirmRepository tokenConfirmRepository;
    private final MailService mailService;

    @Override
    public void login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("currentUser", authentication.getName());
        } catch (DisabledException e) {
            throw new BadRequestException("Tài khoản chưa được kích hoạt");
        } catch (AuthenticationException e) {
            throw new BadRequestException("Email hoặc mật khẩu không đúng");
        }
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email đã được đăng ký");
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Số điện thoại đã được đăng ký");
        }

        if (!request.getConfirmPassword().equals(request.getPassword())){
            throw new BadRequestException("Mật khẩu xác nhận không trùng khớp");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .accountBalance(0)
                .avatar("https://placehold.co/600x400?text=" + String.valueOf(request.getName().charAt(0)).toUpperCase())
                .phoneNumber(request.getPhoneNumber())
                .enabled(false)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        // token xac thuc
        TokenConfirm token = TokenConfirm.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .type(TokenType.REGISTRATION)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        tokenConfirmRepository.save(token);

        // link xac thuc
        String link =   "http://localhost:8080/xac-thuc-tai-khoan?token=" + token.getToken();

        mailService.sendMail1(user, "Xác thực tài khoản", link);
    }

    @Override
    public VerifyResponse confirmRegistration(String token) {
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndType(token,TokenType.REGISTRATION);

        if (tokenConfirmOptional.isEmpty()){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Mã xác thực không hợp lệ")
                    .build();
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedAt()!=null){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Mã xác thực đã được xác thực")
                    .build();
        }

        if (tokenConfirm.getExpiresAt().isBefore(LocalDateTime.now())){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Mã xác thực đã hết hạn")
                    .build();
        }

        User user = tokenConfirm.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);

        return VerifyResponse.builder()
                .success(true)
                .message("Xác thực tài khoản thành công!")
                .build();
    }

    @Override
    public void forgetPassword(ForgetPasswordRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()){
            throw new BadRequestException("Email chưa được đăng ký");
        }

        //Tạo token xác thực đăng kí
        TokenConfirm token = TokenConfirm.builder()
                .token(UUID.randomUUID().toString())
                .user(user.get())
                .type(TokenType.PASSWORD_RESET)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        tokenConfirmRepository.save(token);

        //Tạo link
        String link = "http://localhost:8080/dat-lai-mat-khau?token=" + token.getToken();

        //Gửi mail
        mailService.sendMail2(user.get(), "Thay đổi mật khẩu", link);
    }

    @Override
    public VerifyResponse confirmResetPassword(String token) {
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndType(token,TokenType.PASSWORD_RESET);

        if (tokenConfirmOptional.isEmpty()){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Mã xác thực không hợp lệ")
                    .build();
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        if (tokenConfirm.getConfirmedAt()!=null){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Mã xác thực đã được xác thực")
                    .build();
        }

        if (tokenConfirm.getExpiresAt().isBefore(LocalDateTime.now())){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Mã xác thực đã hết hạn")
                    .build();
        }

        return VerifyResponse.builder()
                .success(true)
                .message("Xác thực tài khoản thành công!")
                .build();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
                .findByTokenAndType(request.getTokenString(),TokenType.PASSWORD_RESET);
        if (tokenConfirmOptional.isEmpty()){
            throw new BadRequestException("Không tìm thấy token");
        }
        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        User user = tokenConfirm.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);
    }
}
