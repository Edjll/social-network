package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSave;
import ru.edjll.backend.validation.exists.Exists;
import ru.edjll.backend.validation.unique.Unique;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWrapperForSave {

    @Length(min = 3, max = 15)
    @Pattern(regexp = "^\\w+$")
    @Unique(table = "user_entity", column = "username")
    private String username;

    @Length(min = 9, max = 30)
    @Email(message = "{user.email.email}")
    @Unique(table = "user_entity", column = "email")
    private String email;

    @Length(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String firstName;

    @Length(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String lastName;

    @Size(min = 1, message = "{user.credentials.size}")
    private List<@Valid CredentialDtoForSave> credentials;

    @Exists(table = "city", column = "id")
    private Long cityId;

    private LocalDate birthday;

    public UserDtoForSave toUserDtoForSave() {
        return new UserDtoForSave(
                username,
                email,
                firstName,
                lastName,
                true,
                credentials
        );
    }

    public UserInfoDtoForSave toUserInfoDtoForSave() {
        return new UserInfoDtoForSave(
                cityId,
                birthday
        );
    }
}
