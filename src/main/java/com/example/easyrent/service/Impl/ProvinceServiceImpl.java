package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.Province;
import com.example.easyrent.exception.ResourceNotFoundException;
import com.example.easyrent.repository.ProvinceRepository;
import com.example.easyrent.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;

    @Override
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }

    @Override
    public Province getProvinceById(Integer id) {
        return provinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id: " + id));
    }
}
