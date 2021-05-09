package ru.edjll.backend.dto.user.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoWrapperForUserCart {

    private List<UserInfoDtoForUserCart> users;
    private int count;
}
