package ru.edjll.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserInfoDto {

    private LocalDate birthday;
    private Long cityId;
}
