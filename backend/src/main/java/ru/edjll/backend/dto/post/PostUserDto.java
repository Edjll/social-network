package ru.edjll.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;

    public PostUserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
    }
}

