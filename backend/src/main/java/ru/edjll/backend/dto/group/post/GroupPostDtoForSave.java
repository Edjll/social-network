package ru.edjll.backend.dto.group.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.entity.GroupPost;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostDtoForSave {

    @NotEmpty
    private String text;

    @NotNull
    @Positive
    @Exists(table = "groups", column = "id")
    private Long groupId;

    public GroupPost toGroupPost() {
        GroupPost groupPost = new GroupPost();
        Group group = new Group();

        group.setId(groupId);

        groupPost.setText(text);
        groupPost.setGroup(group);
        groupPost.setCreatedDate(LocalDateTime.now());

        return groupPost;
    }
}
