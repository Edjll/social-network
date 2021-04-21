package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.service.CountryService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/countries")
@Validated
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Country> getAll() {
        return countryService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Country getById(
            @PathVariable @Positive Long id
    ) {
        return countryService.getById(id);
    }
}
