package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.userInfo.UserInfoDtoForSave;
import ru.edjll.backend.repository.CityRepository;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWrapperForSave {

    @NotEmpty(message = "{user.username.notEmpty}")
    private String username;

    @NotEmpty(message = "{user.email.notEmpty}")
    @Email(message = "{user.email.email}")
    private String email;

    @NotEmpty(message = "{user.firstName.notEmpty}")
    private String firstName;

    @NotEmpty(message = "{user.lastName.notEmpty}")
    private String lastName;

    @Size(min = 1, message = "{user.credentials.size}")
    private List<@Valid CredentialDtoForSave> credentials;

    @Exists(typeRepository = CityRepository.class, message = "{user.cityId.exists}")
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
