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

    public GroupPost toGroupPost() {
        GroupPost groupPost = new GroupPost();

        groupPost.setText(text);
        groupPost.setCreatedDate(LocalDateTime.now());

        return groupPost;
    }
}
