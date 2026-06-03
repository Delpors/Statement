package com.example.statement.util;

import com.example.statement.entity.UserEntity;
import com.example.statement.exceptions.UserNotFoundException;
import com.example.statement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceUtils {

    private final UserRepository userRepository;

    public UserEntity getUserBiIdOrThrow(Long userId) {

        if(userId == null) {
            throw new IllegalArgumentException("User id can not be null");
        }

        return userRepository
                .findById(userId)
                .orElseThrow(
                        ()-> new UserNotFoundException
                                ("User not found by id: " + userId));
    }

}
