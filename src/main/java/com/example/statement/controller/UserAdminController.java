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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {

        model.addAttribute("user", new UserEntity());
        return "createUser";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser (UserRequest request){

        userService.createUser(request);
        return "createUser";
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditUserForm(@PathVariable Long userId, Model model) {

        UserResponse user = userService.getUserById(userId);
        model.addAttribute("user", user);

        return "editUser";
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser (UserRequest request){

        userService.createUser(request);
        return "redirect:/users";
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
        return "redirect:/users";
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String blockUser(@PathVariable Long userId) {

        userService.blockUser(userId);
        return "redirect:/users";
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String unlockUser(@PathVariable Long userId) {

        userService.unlockUser(userId);
        return "redirect:/users";
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model) {

        List<UserResponse> users = userService.getAllActiveUsers();
        model.addAttribute("users", users);

        return "users";
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public String getCountUser() {

        userService.getUsersCount();
        return "redirect:/users";
    }

}
