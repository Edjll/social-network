package ru.edjll.backend.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.City;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDtoForSelect {

    private Long id;
    private String title;

    public CityDtoForSelect(City city) {
        this.id = city.getId();
        this.title = city.getTitle();
    }
}
