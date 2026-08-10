package com.nagorikseba.repository;

import com.nagorikseba.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByNameAndWardId(String name, Long wardId);
    List<Department> findByWardId(Long wardId);
}
