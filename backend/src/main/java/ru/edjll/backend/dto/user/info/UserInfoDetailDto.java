package ru.edjll.backend.dto.user.info;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.city.CityDto;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserInfoDetailDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private LocalDate birthday;
    private CityDto city;

    public UserInfoDetailDto(String id, String firstName, String lastName, String username, String email, LocalDate birthday, Long cityId, String cityTitle, Long countryId, String countryTitle) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.city = new CityDto(cityId, cityTitle, countryId, countryTitle);
    }
}
