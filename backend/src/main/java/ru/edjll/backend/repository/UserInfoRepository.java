package ru.edjll.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.user.info.UserInfoDetailDto;
import ru.edjll.backend.dto.user.info.UserInfoDto;
import ru.edjll.backend.entity.UserInfo;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    @Query( "select new ru.edjll.backend.dto.user.info.UserInfoDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city " +
            "where user.realmId = 'social-network' and user.serviceAccountClientLink is null and user.enabled = true and user.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' and user.username = :username" )
    Optional<UserInfoDto> getUserInfoByUsername(@Param("username") String username);

    @Query( "select new ru.edjll.backend.dto.user.info.UserInfoDetailDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.id end, " +
                "case when city is null then null else city.title end, " +
                "case when country is null then null else country.id end, " +
                "case when country is null then null else country.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city left join city.country country  " +
            "where user.realmId = 'social-network' and user.serviceAccountClientLink is null and user.enabled = true and user.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' and user.id = :id" )
    UserInfoDetailDto getUserInfoDetailById(@Param("id") String id);
}
