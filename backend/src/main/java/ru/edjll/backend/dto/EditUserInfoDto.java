package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class EditUserInfoDto {

    private LocalDate birthday;
    private Long cityId;

    public EditUserInfoDto(LocalDate birthday, Long cityId) {
        this.birthday = birthday;
        this.cityId = cityId;
    }
}
