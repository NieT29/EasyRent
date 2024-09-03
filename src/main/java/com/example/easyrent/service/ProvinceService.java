package com.example.easyrent.service;

import com.example.easyrent.entity.Province;

import java.util.List;

public interface ProvinceService {
    List<Province> getAllProvinces();
    Province getProvinceById(Integer id);
}
