package ru.edjll.backend.dto.city;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.country.CountryDto;

@Data
@NoArgsConstructor
public class CityDto {


    private Long id;
    private String title;
    private CountryDto country;

    public CityDto(Long id, String title, Long countryId, String countryTitle) {
        this.id = id;
        this.title = title;
        this.country = new CountryDto(countryId, countryTitle);
    }
}
