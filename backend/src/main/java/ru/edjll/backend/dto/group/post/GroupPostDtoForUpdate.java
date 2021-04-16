package ru.edjll.backend.dto.group.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.GroupPost;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostDtoForUpdate {

    @NotEmpty
    private String text;

    public GroupPost toGroupPost() {
        GroupPost groupPost = new GroupPost();

        groupPost.setText(text);

        return groupPost;
    }
}
