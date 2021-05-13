package ru.edjll.backend.dto.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.validation.unique.Unique;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDtoForSave {

    @Length(min = 1, max = 20)
    @Unique(table = "country", column = "title")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+\\s{0,1}[a-zA-Zа-яА-Я]+$")
    private String title;

    public CountryDtoForSave(@NotNull Country country) {
        this.title = country.getTitle();
    }

    public Country toCountry() {
        return new Country(title);
    }
}
