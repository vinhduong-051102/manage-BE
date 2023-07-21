package com.example.manage.controller;

import com.example.manage.model.District;
import com.example.manage.model.Province;
import com.example.manage.model.Ward;
import com.example.manage.repository.ProvinceRepository;
import com.example.manage.response.GetLocationResponse;
import com.example.manage.service.DistrictService;
import com.example.manage.service.ProvinceService;
import com.example.manage.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class AddressController {
    private final WardService wardService;
    private final ProvinceRepository provinceRepository;
    private final ProvinceService provinceService;
    private final DistrictService districtService;

    @GetMapping("/province")
    public ResponseEntity<List<GetLocationResponse>> getListProvince(@RequestParam(name = "code", defaultValue = "") String code) {
        if(!Objects.equals(code, "")) {
            Province province = provinceService.findProvinceByCode(code);
            List<GetLocationResponse> responses = new ArrayList<>();
            responses.add(
                    GetLocationResponse
                            .builder()
                            .fullName(province.getFullName())
                            .code(province.getCode())
                            .build()
            );

            return ResponseEntity.ok(responses);
        } else {
            List<Province> provinceList = provinceRepository.findAll();
            List<GetLocationResponse> responses = new ArrayList<>();
            for (Province p:
                    provinceList) {
                responses.add(
                        GetLocationResponse
                                .builder()
                                .fullName(p.getFullName())
                                .code(p.getCode())
                                .build()
                );
            }
            return ResponseEntity.ok(responses);
        }
    }

    @GetMapping("/district")
    public ResponseEntity<List<GetLocationResponse>> getListDistrict(@RequestParam(name = "code") String code) {
        List<District> districtList = districtService.findByProvinceCode(code);
        List<GetLocationResponse> responses = new ArrayList<>();
        for (District d :
                districtList) {
            responses.add(
                    GetLocationResponse
                            .builder()
                            .fullName(d.getFullName())
                            .code(d.getCode())
                            .build()
            );
        }
        return ResponseEntity.ok(responses);
    }

        @GetMapping("/ward")
        public ResponseEntity<List<GetLocationResponse>> getListWard(@RequestParam(name = "code") String code) {
            List<Ward> wardList = wardService.findByDistrictCode(code);
            List<GetLocationResponse> responses = new ArrayList<>();
            for (Ward w:
                    wardList) {
                responses.add(
                        GetLocationResponse
                                .builder()
                                .fullName(w.getFullName())
                                .code(w.getCode())
                                .build()
                );
            }
            return ResponseEntity.ok(responses);
    }
}
