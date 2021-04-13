package ru.edjll.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Post;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoForSave {

    @NotEmpty(message = "{post.userId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}")
    private String userId;

    @NotEmpty(message = "{post.text.notEmpty}")
    private String text;

    public PostDtoForSave(Post post) {
        this.userId = post.getUser().getId();
        this.text = post.getText();
    }

    public Post toPost() {
        Post post = new Post();
        User user = new User();

        user.setId(userId);
        post.setUser(user);
        post.setText(text);
        post.setCreatedDate(LocalDateTime.now());

        return post;
    }
}
