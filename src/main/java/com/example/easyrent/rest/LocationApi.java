package com.example.easyrent.rest;

import com.example.easyrent.entity.District;

import com.example.easyrent.entity.Province;
import com.example.easyrent.entity.Ward;
import com.example.easyrent.service.DistrictService;
import com.example.easyrent.service.ProvinceService;
import com.example.easyrent.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/location")
public class LocationApi {
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;

    @GetMapping("/provinces")
    public List<Province> getProvinces() {
        return provinceService.getAllProvinces();
    }

    @GetMapping("/districts")
    public List<District> getDistricts(@RequestParam Integer provinceId) {
        return districtService.getDistrictsByProvince(provinceId);
    }

    @GetMapping("/wards")
    public List<Ward> getWards(@RequestParam Integer districtId) {
        return wardService.getWardsByDistrict(districtId);
    }
}
