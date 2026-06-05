package com.example.statement.service;

import com.example.statement.dto.request.RegisterRequest;
import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface IUserService {

    void createUser(RegisterRequest request);
    UserResponse getUserById(Long userId);
    UserEntity getUserByUserName(String userName);
    List<UserResponse> getAllActiveUsers();
    void blockUser(Long userId);
    void unlockUser(Long userId);
    void deleteUser(Long userId);
    long getUsersCount();
    boolean isEmailExists(String email);
    boolean isUsernameExists(String s);
}
