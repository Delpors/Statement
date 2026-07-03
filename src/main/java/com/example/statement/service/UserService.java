package com.example.statement.service;

import com.example.statement.dto.request.RegisterRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import com.example.statement.exceptions.UserNotFoundException;
import com.example.statement.repository.UserRepository;
import com.example.statement.service.converter.UserMapper;
import com.example.statement.util.UserRole;
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
    public void createUser(RegisterRequest request) {

        log.info("Попытка создания нового пользователя {}.", request.getUserName());

        boolean isFirstUser = userRepository.count() == 0;
        UserRole role = isFirstUser ? UserRole.ADMIN : UserRole.USER;
        UserEntity userEntity = userMapper.toEntity(request, role);
        userRepository.save(userEntity);

        log.debug("Пользователь {} создан", request.getUserName());
    }

    @Override
    public UserResponse getUserById(Long userId) {

        UserEntity user = userServiceUtils.getUserBiIdOrThrow(userId);
        return userMapper.toDTO(user);
    }

    @Override
    public UserEntity getUserByUserName(String userName) {

        if(userName == null) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        return userRepository.getUserEntityByUsername(userName)
                .orElseThrow(()-> new UserNotFoundException("Не найден пользователь с именем: " + userName));

    }

    @Override
    public List<UserResponse> getAllActiveUsers() {

        List<UserEntity> userEntities = userRepository.findAllByIsActiveIsTrue();

        return userMapper.toListDTO(userEntities);
    }

    @Override
    @Transactional
    public void blockUser(Long userId) {

        log.info("Попытка заблокировать пользователя с id: {} ", userId);
        UserEntity user = userServiceUtils.getUserBiIdOrThrow(userId);
        user.setActive(false);
        log.info("Пользователь c id: {}, успешно заблокирован", userId);
    }

    @Override
    @Transactional
    public void unlockUser(Long userId) {

        log.info("Попытка разблокировать пользователя с id: {} ", userId);
        UserEntity user = userServiceUtils.getUserBiIdOrThrow(userId);
        user.setActive(true);
        log.info("Пользователь {} разблокирован", userId);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        log.info("Попытка удалить пользователя с id: {} ", userId);
        if (userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }else {
            throw new UserNotFoundException("Не найден пользователь с id: " + userId);
        };

        log.info("Пользователь {} удален", userId);
    }

    @Override
    public long getUsersCount() {

        return userRepository.count();
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUsernameExists(String s) {
        return userRepository.existsByUsername(s);
    }
}
