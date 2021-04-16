package ru.edjll.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForAdminPage {

    private Long id;
    private String title;
    private String description;
    private String address;
    private Boolean enabled;
}
