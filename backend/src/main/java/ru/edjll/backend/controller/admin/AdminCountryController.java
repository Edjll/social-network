package ru.edjll.backend.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.country.CountryDtoForSave;
import ru.edjll.backend.dto.country.CountryDtoForUpdate;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.service.CountryService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/admin/countries")
public class AdminCountryController {

    private final CountryService countryService;

    public AdminCountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Country> get(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<String> idDirection,
            @RequestParam(required = false) Optional<String> titleDirection,
            @RequestParam(defaultValue = "0") Long id,
            @RequestParam(defaultValue = "") String title
    ) {
        return countryService.getAll(page, size, idDirection, titleDirection, id, title);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid CountryDtoForSave countryDtoForSave) {
        countryService.save(countryDtoForSave);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable
            @NotNull(message = "{country.id.notNull}")
            @Positive(message = "{country.id.positive}")
            @Exists(table = "country", column = "id", message = "{country.id.exists}") Long id,
            @RequestBody
            @Valid CountryDtoForUpdate countryDtoForUpdate) {
        countryService.update(id, countryDtoForUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            @NotNull(message = "{country.id.null}")
            @Positive(message = "{country.id.positive}")
            @Exists(table = "country", column = "id", message = "{country.id.exists}") Long id
    ) {
        countryService.delete(id);
    }
}
