package com.example.easyrent.service.Impl;

import com.example.easyrent.exception.BadRequestException;
import com.example.easyrent.model.request.LoginRequest;
import com.example.easyrent.repository.UserRepository;
import com.example.easyrent.service.AuthService;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final AuthenticationManager authenticationManager;

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
}
