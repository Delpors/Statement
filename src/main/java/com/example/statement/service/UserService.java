package com.example.statement.service;

import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import com.example.statement.repository.UserRepository;
import com.example.statement.util.UserMapper;
import com.example.statement.util.userMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createUser(UserRequest request) {

    }

    @Override
    public UserResponse getUserById(Long userId) {
        return null;
    }

    @Override
    public List<UserResponse> getAllActiveUsers() {
        return List.of();
    }

    @Override
    @Transactional
    public void blockUser(Long userId) {

    }

    @Override
    @Transactional
    public void unblockUser(Long userId) {

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

    }

    @Override
    public long getUsersCount(Long userId) {
        return 0;
    }
}
