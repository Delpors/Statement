package com.example.statement.payroll;

import com.example.statement.employees.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<PayrollEntity, Long> {

    boolean existsByPaymentDate(LocalDate payrollData);

    Optional<PayrollEntity> findByPaymentDate(LocalDate paymentData);

    @Query("SELECT e FROM PayrollEntity e WHERE e.institution.institutionId = :institutionId")
    List<PayrollEntity> findAllByInstitutionId(@Param("institutionId") Long institutionId);

}
