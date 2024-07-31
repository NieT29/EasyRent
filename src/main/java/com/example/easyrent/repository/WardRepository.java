package com.example.easyrent.repository;

import com.example.easyrent.entity.District;
import com.example.easyrent.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findByDistrict(District district);
}
