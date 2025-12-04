package com.example.statement.employees;

import com.example.statement.institution.InstitutionDTO;
import com.example.statement.institution.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAllByActive(Boolean active);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.institution.institutionId = :institutionId AND e.active = true")
    List<EmployeeEntity> findActiveByInstitutionId(@Param("institutionId") Long institutionId);
}
