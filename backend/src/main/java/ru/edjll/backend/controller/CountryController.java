package ru.edjll.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    public Country getOne(@PathVariable Long id) {
        return countryService.getOne(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public void save(@RequestBody Country country) {
        countryService.save(country);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public void update(@RequestBody Country country) {
        countryService.update(country);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public void delete(@RequestBody Long id) {
        countryService.delete(id);
    }
}
