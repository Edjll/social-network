package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CountryDto {

    private Long id;
    private String title;

    public CountryDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
