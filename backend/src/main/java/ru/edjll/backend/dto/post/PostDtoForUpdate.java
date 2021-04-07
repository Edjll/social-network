package ru.edjll.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Post;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.PostRepository;
import ru.edjll.backend.repository.UserRepository;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoForUpdate {

    @NotNull(message = "{post.id.notNull}")
    @Positive(message = "{post.id.positive}")
    @Exists(table = "post", column = "id", message = "{post.id.exists}")
    private Long id;

    @NotEmpty(message = "{post.userId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}")
    private String userId;

    @NotEmpty(message = "{post.text.notEmpty}")
    private String text;

    @NotNull(message = "{post.createdDate.notNull}")
    private LocalDateTime createdDate;

    public PostDtoForUpdate(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.text = post.getText();
    }

    public Post toPost() {
        Post post = new Post();
        User user = new User();

        user.setId(userId);
        post.setId(id);
        post.setUser(user);
        post.setText(text);
        post.setCreatedDate(createdDate);
        post.setModifiedDate(LocalDateTime.now());

        return post;
    }
}
