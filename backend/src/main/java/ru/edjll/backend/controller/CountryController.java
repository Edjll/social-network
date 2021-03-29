package ru.edjll.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.service.CountryService;

import java.util.Collection;

@RestController
@RequestMapping("/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public Collection<Country> getAll() {
        return countryService.getAll();
    }

    @GetMapping("/{id}")
    public Country getOne(@PathVariable long id) {
        return countryService.getOne(id);
    }
}
