package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.repository.CityRepository;

import java.util.Collection;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Collection<City> getAll() {
        return cityRepository.findAll();
    }

    public Collection<City> getAll(Long countryId) {
        if (countryId == null) return this.getAll();
        return cityRepository.findAllByCountryId(countryId);
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }
}
