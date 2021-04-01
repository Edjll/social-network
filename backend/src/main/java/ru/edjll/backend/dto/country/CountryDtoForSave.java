package ru.edjll.backend.dto.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Country;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDtoForSave {

    @NotEmpty(message = "{country.title.notEmpty}")
    private String title;

    public CountryDtoForSave(@NotNull Country country) {
        this.title = country.getTitle();
    }

    public Country toCountry() {
        return new Country(title);
    }
}
