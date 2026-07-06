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

    @Query("SELECT e FROM EmployeeEntity e WHERE e.id = :employeeId AND e.institution.id = :instId")
    Optional<EmployeeEntity> findByEmployeeIdAndInstId(@Param("employeeId") Long employeeId, @Param("instId") Long instId);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.institution.id = :instId AND e.active = true")
    List<EmployeeEntity> findActiveByInstId(@Param("instId") Long instId);

    @Query("SELECT count (e) FROM EmployeeEntity e WHERE e.institution.id = :instId AND e.active = true ")
    Long countAllByInstitutionAndActiveTrue(@Param("instId") Long instId);
}

