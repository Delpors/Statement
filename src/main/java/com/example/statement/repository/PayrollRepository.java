package com.example.statement.repository;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<PayrollEntity, Long> {

    Optional<PayrollEntity> findByPaymentDateAndInstitution(LocalDate paymentDate, InstitutionEntity institution);

    @Query("SELECT e FROM PayrollEntity e WHERE e.institution.institutionId = :institutionId")
    Optional<List<PayrollEntity>> findAllByInstitutionId(@Param("institutionId") Long institutionId);

}
