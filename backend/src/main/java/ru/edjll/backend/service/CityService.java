package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.city.CityDto;
import ru.edjll.backend.dto.city.CityDtoForSave;
import ru.edjll.backend.dto.city.CityDtoForUpdate;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.exception.ResponseParameterException;
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

    public Page<CityDto> getAll(
            Integer page, Integer size,
            Optional<String> idDirection, Optional<String> titleDirection, Optional<String> countryDirection,
            Integer id, String title, String country
    ) {
        List<Sort.Order> orders = new ArrayList<>();

        idDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "id")));
        countryDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "country")));
        titleDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "title")));

        if (orders.isEmpty()) {
            return cityRepository.search(id, title, country, PageRequest.of(page, size));
        } else {
            return cityRepository.search(id, title, country, PageRequest.of(page, size, Sort.by(orders)));
        }
    }

    public Collection<CityDto> getAll(Optional<Long> countryId) {
        return countryId.map(cityRepository::findAllDtoByCountryId)
                .orElseGet(cityRepository::findAllDto);
    }

    public CityDto getDtoById(Long id) {
        return cityRepository.findDtoById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));
    }

    public City getById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));
    }

    public void save(CityDtoForSave cityDtoForSave) {
        cityRepository.save(cityDtoForSave.toCity());
    }

    public void update(Long id, CityDtoForUpdate cityDtoForUpdate) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!city.getTitle().equals(cityDtoForUpdate.getTitle())
                && cityRepository.existsByTitleAndIdNot(cityDtoForUpdate.getTitle(), city.getId())
        ) {
            throw new ResponseParameterException(HttpStatus.BAD_REQUEST, "title", cityDtoForUpdate.getTitle(), "unique");
        }

        Country country = new Country();
        country.setId(cityDtoForUpdate.getCountryId());

        city.setTitle(cityDtoForUpdate.getTitle());
        city.setCountry(country);

        cityRepository.save(city);
    }

    public void delete(Long id) {
        cityRepository.deleteById(id);
    }
}
