package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.edjll.backend.entity.User;

@Getter
@Setter
@NoArgsConstructor
public class MessageUserInfoDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;

    public MessageUserInfoDto(String id, String firstName, String lastName, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public MessageUserInfoDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
    }
}
