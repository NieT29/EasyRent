package com.example.easyrent.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/quan-ly")
public class UserController {
    @GetMapping
    public String getHome() {
        return "user/pages/index";
    }

    @GetMapping("/thong-tin-ca-nhan")
    public String getUserInfor() {
        return "user/pages/user-infor";
    }

    @GetMapping("/doi-mat-khau")
    public String changePassword() {
        return "user/pages/change-password";
    }
}
