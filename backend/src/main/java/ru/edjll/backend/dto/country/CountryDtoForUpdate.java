package ru.edjll.backend.dto.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDtoForUpdate {

    @NotNull(message = "{country.id.notNull}")
    @Positive(message = "{country.id.positive}")
    @Exists(table = "country", column = "id", message = "{country.id.exists}")
    private Long id;

    @NotEmpty(message = "{country.title.notEmpty}")
    private String title;

    public CountryDtoForUpdate(@NotNull Country country) {
        this.id = country.getId();
        this.title = country.getTitle();
    }

    public Country toCountry() {
        return new Country(id, title);
    }
}
