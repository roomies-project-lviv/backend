package com.roomies.backend.services;

import com.roomies.backend.dto.CityDto;
import com.roomies.backend.exceptions.ResourceNotFoundException;
import com.roomies.backend.models.City;
import com.roomies.backend.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Вказує Spring, що це клас бізнес-логіки
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    // Отримати всі міста (перетворюємо список Entity на список DTO)
    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(this::convertToDto) // Кожне місто з БД перетворюємо на DTO
                .collect(Collectors.toList());
    }

    // Отримати місто за ID
    public CityDto getCityById(Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        return convertToDto(city);
    }

    // Створити нове місто (перетворюємо DTO на Entity, зберігаємо, і повертаємо DTO)
    public CityDto createCity(CityDto cityDto) {
        City newCity = new City();
        newCity.setName(cityDto.getName()); // ID не встановлюємо, БД сама його згенерує

        City savedCity = cityRepository.save(newCity);
        return convertToDto(savedCity);
    }

    // Оновити існуюче місто
    public CityDto updateCity(Integer id, CityDto cityDto) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        existingCity.setName(cityDto.getName());
        City updatedCity = cityRepository.save(existingCity);

        return convertToDto(updatedCity);
    }

    // Видалити місто
    public void deleteCity(Integer id) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        cityRepository.delete(existingCity);
    }

    // --- ДОПОМІЖНІ МЕТОДИ (Мапінг) ---
    // Вони відповідають за перекладання даних з однієї коробки в іншу

    private CityDto convertToDto(City city) {
        return new CityDto(city.getId(), city.getName());
    }
}