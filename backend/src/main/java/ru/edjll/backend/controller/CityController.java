package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.service.CityService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
@Validated
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<City> getCities(@RequestParam(required = false) Optional<Long> countryId) {
        return cityService.getAll(countryId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<City> getById(
            @PathVariable
            @NotNull(message = "{city.id.notNull}")
            @Positive(message = "{city.id.positive}")
            @Exists(table = "city", column = "id", message = "{city.id.exists}") Long id
    ) {
        return cityService.getById(id);
    }
}
