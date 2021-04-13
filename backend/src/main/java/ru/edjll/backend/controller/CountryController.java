package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.country.CountryDtoForSave;
import ru.edjll.backend.dto.country.CountryDtoForUpdate;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.service.CountryService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/country")
@Validated
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Country> getAll() {
        return countryService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Country> getById(
            @PathVariable
            @NotNull(message = "{country.id.notNull}")
            @Positive(message = "{country.id.positive}")
            @Exists(table = "country", column = "id", message = "{country.id.exists}") Long id
    ) {
        return countryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<Country> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<String> idDirection,
            @RequestParam(required = false) Optional<String> titleDirection,
            @RequestParam(defaultValue = "0") Long id,
            @RequestParam(defaultValue = "") String title
    ) {
        return countryService.getAll(page, size, idDirection, titleDirection, id, title);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid CountryDtoForSave countryDtoForSave) {
        countryService.save(countryDtoForSave);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid CountryDtoForUpdate countryDtoForUpdate) {
        countryService.update(countryDtoForUpdate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam
            @NotNull(message = "{country.id.null}")
            @Positive(message = "{country.id.positive}")
            @Exists(table = "country", column = "id", message = "{country.id.exists}") Long id
    ) {
        countryService.delete(id);
    }
}
