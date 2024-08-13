package com.example.easyrent.service.Impl;

import com.example.easyrent.entity.Ward;
import com.example.easyrent.repository.WardRepository;
import com.example.easyrent.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {
    private final WardRepository wardRepository;

    @Override
    public List<Ward> getWardsByDistrict(Integer districtId) {
        return wardRepository.findByDistrictId(districtId);
    }
}
