package com.example.statement.controller;

import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import com.example.statement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "createUser";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@ModelAttribute UserRequest request) {
        userService.createUser(request);
        return "redirect:/api/users";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model) {

        List<UserResponse> users = userService.getAllActiveUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public String getCountUser(Model model) {

        long count = userService.getUsersCount();
        model.addAttribute("count", count);
        return "redirect:/users";
    }

    @GetMapping("/{userId}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditUserForm(@PathVariable Long userId, Model model) {

        UserResponse user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "editUser";
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
        return "redirect:/api/users";
    }

    @PostMapping("/{userId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public String blockUser(@PathVariable Long userId) {

        userService.blockUser(userId);
        return "redirect:/api/users";
    }

    @PostMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public String unlockUser(@PathVariable Long userId) {

        userService.unlockUser(userId);
        return "redirect:/api/users";
    }
}
