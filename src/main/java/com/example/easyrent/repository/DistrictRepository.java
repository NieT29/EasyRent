package com.example.easyrent.repository;

import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByProvince(Province province);
}
