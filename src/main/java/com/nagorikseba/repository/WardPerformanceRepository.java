package com.nagorikseba.repository;

import com.nagorikseba.entity.WardPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardPerformanceRepository extends JpaRepository<WardPerformance, Long> {
    Optional<WardPerformance> findByWardIdAndMonthAndYear(Long wardId, Integer month, Integer year);
    List<WardPerformance> findByWardId(Long wardId);
}
