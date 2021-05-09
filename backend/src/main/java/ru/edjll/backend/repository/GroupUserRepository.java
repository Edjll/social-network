package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.entity.GroupUser;
import ru.edjll.backend.entity.GroupUserKey;

import javax.persistence.SqlResultSetMapping;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserKey> {

    @Modifying
    @Transactional
    void deleteAllByIdGroupId(Long groupId);

    @Query(nativeQuery = true)
    List<UserInfoDtoForSearch> getAllDtoByAuthorizedUser(
            @Param("user_id") String userId,
            @Param("group_id") Long groupId,
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("city_id") Long cityId,
            @Param("country_id") Long countryId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<UserInfoDtoForSearch> getAllDtoByAnonymousUser(
            @Param("group_id") Long groupId,
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("city_id") Long cityId,
            @Param("country_id") Long countryId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<GroupUserDtoForGroupPage> getCardDtoByAuthorizedUser(
            @Param("group_id") Long groupId,
            @Param("user_id") String userId,
            @Param("size") Integer size
    );

    @Query("select new ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage(gu.id.user.username, gu.id.user.firstName) " +
            "from GroupUser gu join gu.id.user " +
            "where gu.id.group.id = :group_id")
    List<GroupUserDtoForGroupPage> getCardDtoByAnonymousUser(
            @Param("group_id") Long groupId,
            Pageable pageable
    );

    Integer countByIdGroupId(Long groupId);

    Integer countByIdUserId(String userId);
}
