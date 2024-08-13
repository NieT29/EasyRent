package com.example.easyrent.service;

import com.example.easyrent.entity.Ward;

import java.util.List;

public interface WardService {
    List<Ward> getWardsByDistrict(Integer districtId);
}
