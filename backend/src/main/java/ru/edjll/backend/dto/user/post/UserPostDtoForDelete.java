package ru.edjll.backend.dto.user.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.UserPost;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDtoForDelete {

    @NotNull(message = "{post.id.notNull}")
    @Positive(message = "{post.id.positive}")
    @Exists(table = "post", column = "id", message = "{post.id.exists}")
    private Long id;

    @NotEmpty(message = "{post.userId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}")
    private String userId;

    public UserPostDtoForDelete(UserPost userPost) {
        this.id = userPost.getId();
        this.userId = userPost.getUser().getId();
    }

    public UserPost toPost() {
        UserPost userPost = new UserPost();
        User user = new User();

        user.setId(userId);
        userPost.setId(id);
        userPost.setUser(user);

        return userPost;
    }
}
