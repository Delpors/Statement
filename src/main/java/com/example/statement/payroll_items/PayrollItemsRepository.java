package com.example.statement.payroll_items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PayrollItemsRepository extends JpaRepository<PayrollItemsEntity, Long> {

    @Query("SELECT pi FROM PayrollItemsEntity pi " +
    "WHERE pi.payroll.payrollid = :payrollId " +
    "AND pi.institution.institutionId = :selectedInst " +
    "ORDER BY pi.totalIssued ASC")
    Page<PayrollItemsEntity> getAllByPayrollItemIdAndInstitutionId(@Param("payrollId") Long payrollId,
                                                        @Param("selectedInst") Long selectedInst,
                                                        Pageable pageable);

    void deleteByPayrollItemId(Long payrollItemId);
}
