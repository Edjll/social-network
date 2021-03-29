package ru.edjll.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.service.CityService;

import java.util.Collection;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/all")
    public Collection<City> getAll(@RequestParam(required = false) Long countryId) {
        return cityService.getAll(countryId);
    }
}
