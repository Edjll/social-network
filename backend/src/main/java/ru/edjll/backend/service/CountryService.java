package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.country.CountryDtoForSave;
import ru.edjll.backend.dto.country.CountryDtoForUpdate;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.repository.CountryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Collection<Country> getAll() {
        return countryRepository.findAll();
    }

    public Page<Country> getAll(
            Integer page, Integer size,
            Optional<String> idDirection, Optional<String> titleDirection,
            Long id, String title
    ) {
        List<Sort.Order> orders = new ArrayList<>();

        idDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "id")));
        titleDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "title")));

        if (orders.isEmpty()) {
            return countryRepository.findAllByIdGreaterThanEqualAndTitleStartingWithIgnoreCase(id, title, PageRequest.of(page, size));
        } else {
            return countryRepository.findAllByIdGreaterThanEqualAndTitleStartingWithIgnoreCase(id, title, PageRequest.of(page, size, Sort.by(orders)));
        }
    }

    public Optional<Country> getById(Long id) {
        return countryRepository.findById(id);
    }

    public void save(CountryDtoForSave countryDtoForSave) {
        countryRepository.save(countryDtoForSave.toCountry());
    }

    public void update(CountryDtoForUpdate countryDtoForUpdate) {
        countryRepository.save(countryDtoForUpdate.toCountry());
    }

    public void delete(Long id) {
        countryRepository.deleteById(id);
    }
}
