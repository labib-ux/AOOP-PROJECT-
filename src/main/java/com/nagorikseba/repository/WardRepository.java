package com.nagorikseba.repository;

import com.nagorikseba.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    Optional<Ward> findByWardNumberAndCityCorporation(Integer wardNumber, String cityCorporation);
    List<Ward> findByCityCorporation(String cityCorporation);
}
