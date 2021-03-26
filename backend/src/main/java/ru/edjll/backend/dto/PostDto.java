package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto user;

    public PostDto(Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate, String userId, String firstName, String lastName, String username) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.user = new UserDto(userId, firstName, lastName, username);
    }
}
