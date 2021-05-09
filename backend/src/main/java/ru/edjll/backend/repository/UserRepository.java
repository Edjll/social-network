package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.InterlocutorDto;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Boolean existsByEmailAndIdNot(String username, String id);

    @Query(nativeQuery = true)
    List<UserInfoDtoForSearch> searchUsersByAuthorizedUser(
            @Param("user_id") String userId,
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("city_id") Long cityId,
            @Param("country_id") Long countryId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<UserInfoDtoForSearch> searchUsersByAnonymousUser(
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("city_id") Long cityId,
            @Param("country_id") Long countryId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    Page<UserDtoForAdminPage> getUsersForAdmin(
            @Param("id") String id,
            @Param("username") String username,
            @Param("email") String email,
            @Param("city") String city,
            @Param("id_direction") String idDirection,
            @Param("username_direction") String usernameDirection,
            @Param("email_direction") String emailDirection,
            @Param("city_direction") String cityDirection,
            @Param("enabled_direction") String enabledDirection,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<PostDto> getFeed(
            @Param("id") String id,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<InterlocutorDto> getInterlocutors(
            @Param("id") String id,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    InterlocutorDto getInterlocutor(
            @Param("id") String id,
            @Param("interlocutor_id") String interlocutorId
    );
}
