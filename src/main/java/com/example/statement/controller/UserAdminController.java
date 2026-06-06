package com.example.statement.controller;

import com.example.statement.dto.request.RegisterRequest;
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
@RequestMapping("/admin/users")
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
    public String createUser(@ModelAttribute RegisterRequest request) {
        userService.createUser(request);
        return "redirect:/admin/users/list";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model) {

        List<UserResponse> users = userService.getAllActiveUsers();
        model.addAttribute("users", users);
        return "userlist";
    }

    @GetMapping("/{userId}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditUserForm(@PathVariable Long userId, Model model) {

        UserResponse user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
        return "redirect:/admin/users/list";
    }

    @PostMapping("/{userId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public String blockUser(@PathVariable Long userId) {

        userService.blockUser(userId);
        return "redirect:/admin/users/list";
    }

    @PostMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public String unlockUser(@PathVariable Long userId) {

        userService.unlockUser(userId);
        return "redirect:/admin/users/list";
    }
}
