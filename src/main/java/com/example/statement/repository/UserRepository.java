package com.example.statement.repository;

import com.example.statement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByIsActiveIsTrue();

    Optional<UserEntity> getUserEntityByUsername(String username);
}
