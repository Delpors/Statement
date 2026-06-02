package com.example.statement.util;

import com.example.statement.dto.request.UserRequest;
import com.example.statement.dto.response.UserResponse;
import com.example.statement.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toDTO(UserEntity userEntity) {

        if (userEntity == null) return null;

        return new UserResponse(
                userEntity.getId(),
                userEntity.getUsername(),
                null,
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.getCreated(),
                userEntity.getDeleted(),
                userEntity.isActive()
        );
    }

    public UserEntity toEntity(UserRequest userRequest) {

        if (userRequest == null) return null;

        return new UserEntity(
                null,
                userRequest.userName(),
                userRequest.password(),
                userRequest.email(),
                userRequest.role(),
                userRequest.createdAt(),
                userRequest.deletedAt(),
                null,
                userRequest.isActive()
        );
    }

    public List<UserResponse> toListDTO(List<UserEntity> userEntityList) {

        if (userEntityList == null) return List.of();

        return userEntityList
                .stream()
                .map( this::toDTO)
                .collect(Collectors.toList());
    }
}
