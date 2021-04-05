package ru.edjll.backend.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Post;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private PostUserDto user;

    public PostDto(Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate, String userId, String firstName, String lastName, String username) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.user = new PostUserDto(userId, firstName, lastName, username);
    }

    public PostDto(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
        this.user = new PostUserDto(post.getUser());
    }
}
