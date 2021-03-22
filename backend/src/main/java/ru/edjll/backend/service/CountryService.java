package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.repository.CountryRepository;

import java.util.Collection;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Collection<Country> getAll() {
        return countryRepository.findAll();
    }

    public Country getOne(long id) {
        return countryRepository.getOne(id);
    }
}
