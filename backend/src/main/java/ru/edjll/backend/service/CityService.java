package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.city.CityDtoForSave;
import ru.edjll.backend.dto.city.CityDtoForUpdate;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.repository.CityRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Collection<City> getAll() {
        return cityRepository.findAll();
    }

    public Page<City> getAll(
            Integer page, Integer size,
            Optional<String> idDirection, Optional<String> titleDirection, Optional<String> countryDirection,
            Long id, String title, String country
    ) {
        List<Sort.Order> orders = new ArrayList<>();

        idDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "id")));
        countryDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "country")));
        titleDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "title")));

        if (orders.isEmpty()) {
            return cityRepository.findAllByIdGreaterThanEqualAndTitleStartingWithIgnoreCaseAndCountryTitleStartingWithIgnoreCase(id, title, country, PageRequest.of(page, size));
        } else {
            return cityRepository.findAllByIdGreaterThanEqualAndTitleStartingWithIgnoreCaseAndCountryTitleStartingWithIgnoreCase(id, title, country, PageRequest.of(page, size, Sort.by(orders)));
        }
    }

    public Collection<City> getAll(Optional<Long> countryId) {
        return countryId.map(cityRepository::findAllByCountryId)
                .orElseGet(this::getAll);
    }

    public Optional<City> getById(Long id) {
        return cityRepository.findById(id);
    }

    public void save(CityDtoForSave cityDtoForSave) {
        cityRepository.save(cityDtoForSave.toCity());
    }

    public void update(CityDtoForUpdate cityDtoForUpdate) {
        cityRepository.save(cityDtoForUpdate.toCity());
    }

    public void delete(Long id) {
        cityRepository.deleteById(id);
    }
}
