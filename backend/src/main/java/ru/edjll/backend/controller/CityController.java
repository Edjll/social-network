package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.service.CityService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all")
    public Collection<City> getAll(@RequestParam(required = false) Optional<Long> countryId) {
        return cityService.getAll(countryId);
    }

    @GetMapping("/{id}")
    public City getById(@PathVariable Long id) {
        return cityService.getById(id);
    }

    @GetMapping("/page")
    public Page<City> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<String> idDirection,
            @RequestParam(required = false) Optional<String> titleDirection,
            @RequestParam(required = false) Optional<String> countryDirection,
            @RequestParam(defaultValue = "0") Long id,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String country
    ) {
        return cityService.getAll(page, size, idDirection, titleDirection, countryDirection, id, title, country);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public void save(@RequestBody City city) {
        cityService.save(city);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public void update(@RequestBody City city) {
        cityService.update(city);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        cityService.delete(id);
    }
}
