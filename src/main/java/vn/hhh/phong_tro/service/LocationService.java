package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.response.location.DistrictDto;
import vn.hhh.phong_tro.dto.response.location.WardDto;
import vn.hhh.phong_tro.model.City;
import vn.hhh.phong_tro.model.District;
import vn.hhh.phong_tro.model.Ward;
import vn.hhh.phong_tro.repository.CityRepository;
import vn.hhh.phong_tro.repository.DistrictRepository;
import vn.hhh.phong_tro.repository.WardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final CityRepository cityRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;

    public List<City> getAllCities() {
        return cityRepo.findAll();
    }

    public List<DistrictDto> getDistrictsByCityId(Long cityId) {
        List<District> list = districtRepo.findByCityId(cityId);
        return list.stream().map(i ->DistrictDto.builder()
                .id(i.getId())
                .name(i.getName())
                .build()).toList();
    }

    public List<WardDto> getWardsByDistrictId(Long districtId) {
        List<Ward> list =  wardRepo.findByDistrictId(districtId);
        return list.stream().map(i -> WardDto.builder()
                .id(i.getId())
                .name(i.getName())
                .build()).toList();

    }
}

