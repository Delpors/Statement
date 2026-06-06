package com.example.statement.util;

import com.example.statement.dto.request.RegisterRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse toDTO(UserEntity userEntity) {

        if (userEntity == null) return null;

        return new UserResponse(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.getCreated(),
                userEntity.getDeleted(),
                userEntity.isActive()
        );
    }

    public UserEntity toEntity(RegisterRequest userRequest, UserRole role) {

        if (userRequest == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.getUserName());
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setRole(role);

        return userEntity;

    }

    public List<UserResponse> toListDTO(List<UserEntity> userEntityList) {

        if (userEntityList == null) return List.of();

        return userEntityList
                .stream()
                .map( this::toDTO)
                .collect(Collectors.toList());
    }
}
