package com.roomies.backend.controllers;

import com.roomies.backend.dto.CityDto;
import com.roomies.backend.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService; // Підключаємо наш Сервіс, а НЕ Репозиторій

    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Integer id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        CityDto createdCity = cityService.createCity(cityDto);
        return new ResponseEntity<>(createdCity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable Integer id, @RequestBody CityDto cityDto) {
        return ResponseEntity.ok(cityService.updateCity(id, cityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}