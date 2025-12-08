package com.example.statement.repository;

import com.example.statement.entity.PeriodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeriodRepository extends JpaRepository<PeriodEntity, Long> {

    Optional<PeriodEntity> findByPeriodId(String periodId);
}
