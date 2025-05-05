package vn.hhh.phong_tro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.hhh.phong_tro.dto.response.location.DistrictDto;
import vn.hhh.phong_tro.dto.response.location.WardDto;
import vn.hhh.phong_tro.model.City;
import vn.hhh.phong_tro.model.District;
import vn.hhh.phong_tro.model.Ward;
import vn.hhh.phong_tro.service.LocationService;
import vn.hhh.phong_tro.util.Uri;

import java.util.List;

@RestController
@RequestMapping(Uri.Location)
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(locationService.getAllCities());
    }
    @GetMapping("/districts")
    public ResponseEntity<List<DistrictDto>> getDistrictsByCity(@RequestParam Long cityId) {
        return ResponseEntity.ok(locationService.getDistrictsByCityId(cityId));
    }
    @GetMapping("/wards")
    public ResponseEntity<List<WardDto>> getWardsByDistrict(@RequestParam Long districtId) {
        return ResponseEntity.ok(locationService.getWardsByDistrictId(districtId));
    }
}
