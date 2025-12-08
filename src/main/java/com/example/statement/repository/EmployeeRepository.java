package com.example.statement.repository;

import com.example.statement.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAllByActive(Boolean active);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.institution.institutionId = :institutionId AND e.active = true")
    List<EmployeeEntity> findActiveByInstitutionId(@Param("institutionId") Long institutionId);
}
