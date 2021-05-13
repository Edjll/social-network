package ru.edjll.backend.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.entity.Country;
import ru.edjll.backend.validation.exists.Exists;
import ru.edjll.backend.validation.unique.Unique;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDtoForSave {

    @Length(min = 1, max = 20)
    @Unique(table = "city", column = "title")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+\\s{0,1}[a-zA-Zа-яА-Я]+$")
    private String title;

    @NotNull
    @Exists(table = "country", column = "id")
    private Long countryId;

    public CityDtoForSave(@NotNull City city) {
        this.title = city.getTitle();
        this.countryId = city.getCountry().getId();
    }

    public City toCity() {
        City city = new City();
        Country country = new Country();

        country.setId(countryId);
        city.setTitle(title);
        city.setCountry(country);

        return city;
    }
}
