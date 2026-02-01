package com.example.statement.repository;

import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAllByActive(Boolean active);

    EmployeeEntity findByEmployeeIdAndInstitution(Long employeeId, InstitutionEntity institution);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.institution.institutionId = :institutionId AND e.active = true")
    Optional<List<EmployeeEntity>> findActiveByInstitutionId(@Param("institutionId") Long institutionId);

    int countAllByInstitutionAndActiveTrue(InstitutionEntity institution);
}

