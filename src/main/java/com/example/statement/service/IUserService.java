package com.example.statement.service;

import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;

import java.util.List;

public interface IUserService {

    void createUser(UserRequest request);
    UserResponse getUserById(Long userId);
    List<UserResponse> getAllActiveUsers();
    void blockUser(Long userId);
    void unblockUser(Long userId);
    void deleteUser(Long userId);
    long getUsersCount(Long userId);
}
