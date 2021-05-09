package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterlocutorDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Integer position;
    private Integer newMessages;

    public InterlocutorDto(String id, String username, String firstName, String lastName, Integer newMessages) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.newMessages = newMessages;
    }
}
