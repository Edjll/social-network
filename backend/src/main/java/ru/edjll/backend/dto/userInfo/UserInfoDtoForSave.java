package ru.edjll.backend.dto.userInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.entity.UserInfo;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoForSave {

    private Long cityId;
    private LocalDate birthday;

    public UserInfoDtoForSave(UserInfo userInfo) {
        this.cityId = userInfo.getCity() != null ? userInfo.getCity().getId() : null;
        this.birthday = userInfo.getBirthday();
    }

    public UserInfo toUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setBirthday(birthday);
        if (cityId != null) userInfo.setCity(new City(cityId));
        return userInfo;
    }
}
