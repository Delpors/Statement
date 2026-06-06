package com.example.statement.repository;

import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long> {

    List<InstitutionEntity> findAllByUser(UserEntity user);
}
