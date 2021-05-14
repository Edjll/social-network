package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.city.CityDto;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.service.CityService;

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
    public Collection<CityDto> getCities(
            @RequestParam(required = false) Optional<Long> countryId
    ) {
        return cityService.getAll(countryId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityDto getById(
            @PathVariable @Positive Long id
    ) {
        return cityService.getDtoById(id);
    }
}
