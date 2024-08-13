package com.example.easyrent.service;

import com.example.easyrent.entity.District;

import java.util.List;

public interface DistrictService {
    List<District> getDistrictsByProvince(Integer provinceId);
}
