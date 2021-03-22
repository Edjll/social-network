package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserInfoDetailDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthday;
    private CityDto city;

    public UserInfoDetailDto(String id, String firstName, String lastName, String username, LocalDate birthday, Long cityId, String cityTitle, Long countryId, String countryTitle) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthday = birthday;
        this.city = new CityDto(cityId, cityTitle, countryId, countryTitle);
    }
}
