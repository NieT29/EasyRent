package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.District;
import com.example.easyrent.exception.ResourceNotFoundException;
import com.example.easyrent.repository.DistrictRepository;
import com.example.easyrent.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;

    @Override
    public List<District> getDistrictsByProvince(Integer provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }

    @Override
    public District getDistrictById(Integer id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + id));
    }
}
