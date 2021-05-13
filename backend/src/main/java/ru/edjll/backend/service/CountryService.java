package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.country.CountryDtoForSave;
import ru.edjll.backend.dto.country.CountryDtoForUpdate;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.exception.ResponseParameterException;
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

    public Country getById(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));
    }

    public void save(CountryDtoForSave countryDtoForSave) {
        countryRepository.save(countryDtoForSave.toCountry());
    }

    public void update(Long id, CountryDtoForUpdate countryDtoForUpdate) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (country.getTitle().equals(countryDtoForUpdate.getTitle())
                && countryRepository.existsByTitleAndIdNot(countryDtoForUpdate.getTitle(), country.getId())
        ) {
            throw new ResponseParameterException(HttpStatus.BAD_REQUEST, "title", countryDtoForUpdate.getTitle(), "unique");
        }

        country.setTitle(countryDtoForUpdate.getTitle());

        countryRepository.save(country);
    }

    public void delete(Long id) {
        countryRepository.deleteById(id);
    }
}
