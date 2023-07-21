package com.example.manage.repository;

import com.example.manage.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findAllByProvinceCode(String code);

    Optional<District> findByCode(String code);
}
