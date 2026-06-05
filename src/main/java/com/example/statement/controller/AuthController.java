package com.example.statement.controller;

import com.example.statement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "expired", required = false) String expired,
                            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Вы успешно вышли из системы");
        }

        if (expired != null) {
            model.addAttribute("errorMessage", "Ваша сессия истекла. Пожалуйста, войдите снова");
        }

        return "login";
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        Long loginTime = (Long) request.getSession().getAttribute("userLoginTime");
        if (loginTime != null) {
            model.addAttribute("loginTime", loginTime);
        }
        return "home";
    }
}