package com.example.statement.service;

import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;

import java.util.List;

public interface IUserService {

    void createUser(UserRequest request);
    UserResponse getUserById(Long userId);
    UserEntity getUserByUserName(String userName);
    List<UserResponse> getAllActiveUsers();
    void blockUser(Long userId);
    void unlockUser(Long userId);
    void deleteUser(Long userId);
    long getUsersCount();
}
