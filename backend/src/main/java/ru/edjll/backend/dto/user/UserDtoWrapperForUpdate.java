package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSave;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWrapperForUpdate {

    @Length(min = 3, max = 15)
    @Pattern(regexp = "^\\w+$")
    private String username;

    @Length(min = 9, max = 30)
    @Email(message = "{user.email.email}")
    private String email;

    @Length(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String firstName;

    @Length(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String lastName;

    @Exists(table = "city", column = "id")
    private Long cityId;

    private LocalDate birthday;

    public UserDtoForUpdate toUserDtoForUpdate() {
        return new UserDtoForUpdate(
                username,
                email,
                firstName,
                lastName
        );
    }

    public UserInfoDtoForSave toUserInfoDtoForSave() {
        return new UserInfoDtoForSave(
                cityId,
                birthday
        );
    }
}
