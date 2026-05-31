package com.example.statement.repository;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<PayrollEntity, Long> {

    Optional<PayrollEntity> findByPaymentDateAndInstitution(LocalDate paymentDate, InstitutionEntity institution);

    Optional<PayrollEntity> findByMonthAndYearAndInstitution(Integer month, Integer year, InstitutionEntity institution);

    @Query("SELECT e FROM PayrollEntity e WHERE e.institution.id = :institutionId")
    Optional<List<PayrollEntity>> findAllByInstitutionId(@Param("institutionId") Long institutionId);

    @NotNull
    List<PayrollEntity> findAllByYearAndInstitution_id(Integer year, Long institutionId);

    long countAllByInstitution(InstitutionEntity institution);


}
