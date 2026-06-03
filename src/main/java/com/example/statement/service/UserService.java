package com.example.statement.service;

import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import com.example.statement.exceptions.UserNotFoundException;
import com.example.statement.repository.UserRepository;
import com.example.statement.util.UserMapper;
import com.example.statement.util.UserServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserServiceUtils userServiceUtils;

    @Override
    @Transactional
    public void createUser(UserRequest request) {

        UserEntity userEntity = userMapper.toEntity(request);
        userRepository.save(userEntity);
        log.info("User {} created", request.userName());
    }

    @Override
    public UserResponse getUserById(Long userId) {

        UserEntity user = userServiceUtils.getUserBiIdOrThrow(userId);
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserResponse> getAllActiveUsers() {

        List<UserEntity> userEntities = userRepository.findAllByIsActiveIsTrue();

        return userMapper.toListDTO(userEntities);
    }

    @Override
    @Transactional
    public void blockUser(Long userId) {

        UserEntity user = userServiceUtils.getUserBiIdOrThrow(userId);
        user.setActive(false);
        log.info("User {} blocked", userId);
    }

    @Override
    @Transactional
    public void unlockUser(Long userId) {

        UserEntity user = userServiceUtils.getUserBiIdOrThrow(userId);
        user.setActive(true);
        log.info("User {} unblocked", userId);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        if (userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }else {
            throw new UserNotFoundException("User not found by id: " + userId);
        };

        log.info("User {} deleted", userId);
    }

    @Override
    public long getUsersCount() {

        return userRepository.count();
    }
}
