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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoForDelete {

    @NotNull(message = "{post.id.notNull}")
    @Positive(message = "{post.id.positive}")
    @Exists(table = "post", column = "id", message = "{post.id.exists}")
    private Long id;

    @NotEmpty(message = "{post.userId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}")
    private String userId;

    public PostDtoForDelete(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
    }

    public Post toPost() {
        Post post = new Post();
        User user = new User();

        user.setId(userId);
        post.setId(id);
        post.setUser(user);

        return post;
    }
}
