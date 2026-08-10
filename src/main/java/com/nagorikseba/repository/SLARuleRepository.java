package com.nagorikseba.repository;

import com.nagorikseba.entity.SLARule;
import com.nagorikseba.enums.ComplaintCategory;
import com.nagorikseba.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SLARuleRepository extends JpaRepository<SLARule, Long> {
    Optional<SLARule> findByCategoryAndPriority(ComplaintCategory category, Priority priority);
}
