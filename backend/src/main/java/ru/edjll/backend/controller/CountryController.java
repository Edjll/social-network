package ru.edjll.backend.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Collection<Country>> getAll() {
        return ResponseEntity.ok().body(countryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getOne(@PathVariable long id) {
        return ResponseEntity.ok().body(countryService.getOne(id));
    }
}
