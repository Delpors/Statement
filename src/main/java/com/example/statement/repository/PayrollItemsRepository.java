package com.example.statement.repository;

import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollItemsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PayrollItemsRepository extends JpaRepository<PayrollItemsEntity, Long> {

    @Query("SELECT pi FROM PayrollItemsEntity pi " +
    "WHERE pi.payroll.id = :payrollId " +
    "AND pi.institution.id = :selectedInst " +
    "ORDER BY pi.employee.name ASC")
    Page<PayrollItemsEntity> getAllByPayrollItemIdAndInstitutionId(@Param("payrollId") Long payrollId,
                                                        @Param("selectedInst") Long selectedInst,
                                                        Pageable pageable);


    void deleteById(Long id);

    Page<PayrollItemsEntity> findAllByYearAndInstitution(Integer year, InstitutionEntity institution,
                                                               Pageable pageable);
}
