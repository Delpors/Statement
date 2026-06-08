package com.example.statement.controller;

import com.example.statement.dto.request.RegisterRequest;
import com.example.statement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest")
                               RegisterRequest request,
                               BindingResult result,
                               Model model) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Пароли не совпадают");
        }

        if (userService.isUsernameExists(request.getUserName())) {
            result.rejectValue("userName", "error.username", "Пользователь с таким именем уже существует");
        }

        if (result.hasErrors()) {
            return "register";
        }

        userService.createUser(request);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }

        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        } else if (registered != null) {
            model.addAttribute("message", "Регистрация успешна! Войдите в систему");
        }

        return "login";
    }
}