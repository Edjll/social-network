package ru.edjll.backend.dto.group.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserDtoWrapperForGroupPage {

    private List<GroupUserDtoForGroupPage> users;
    private int count;
}
