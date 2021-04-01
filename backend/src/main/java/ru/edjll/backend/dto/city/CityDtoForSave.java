package ru.edjll.backend.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.entity.Country;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDtoForSave {

    @NotEmpty(message = "{city.title.notEmpty}")
    private String title;

    @NotNull(message = "{city.countryId.notNull}")
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
