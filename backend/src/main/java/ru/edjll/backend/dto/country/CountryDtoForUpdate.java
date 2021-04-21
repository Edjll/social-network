package ru.edjll.backend.dto.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDtoForUpdate {

    @Length(min = 1, max = 20)
    private String title;

    public CountryDtoForUpdate(@NotNull Country country) {
        this.title = country.getTitle();
    }

    public Country toCountry() {
        Country country = new Country();
        country.setTitle(title);
        return country;
    }
}
