package ru.edjll.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.UserInfoDetailDto;
import ru.edjll.backend.dto.UserInfoDto;
import ru.edjll.backend.entity.UserInfo;

import java.util.Collection;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    @Query( "select new ru.edjll.backend.dto.UserInfoDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city " +
            "where user.username = :username" )
    UserInfoDto getUserInfoByUsername(@Param("username") String username);

    @Query( "select new ru.edjll.backend.dto.UserInfoDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city " +
            "where lower(user.firstName) like concat('%', lower(:firstName), '%') " +
            "and lower(user.lastName) like concat('%', lower(:lastName), '%')" )
    Collection<UserInfoDto> getUserInfoByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query( "select new ru.edjll.backend.dto.UserInfoDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city left join city.country country " +
            "where lower(user.firstName) like concat('%', lower(:firstName), '%') " +
            "and lower(user.lastName) like concat('%', lower(:lastName), '%') " +
            "and country.id = :countryId" )
    Collection<UserInfoDto> getUserInfoByFirstNameAndLastNameAndCountryId(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("countryId") Long countryId);

    @Query( "select new ru.edjll.backend.dto.UserInfoDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city " +
            "where lower(user.firstName) like concat('%', lower(:firstName), '%') " +
            "and lower(user.lastName) like concat('%', lower(:lastName), '%') " +
            "and city.id = :cityId" )
    Collection<UserInfoDto> getUserInfoByFirstNameAndLastNameAndCityId(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("cityId") Long cityId);

    @Query( "select new ru.edjll.backend.dto.UserInfoDetailDto(" +
                "user.id, user.firstName, user.lastName, user.username, " +
                "case when userInfo is null then null else userInfo.birthday end, " +
                "case when city is null then null else city.id end, " +
                "case when city is null then null else city.title end, " +
                "case when country is null then null else country.id end, " +
                "case when country is null then null else country.title end) " +
            "from User user left join user.userInfo userInfo left join userInfo.city city left join city.country country  " +
            "where user.username = :username" )
    UserInfoDetailDto getUserInfoDetailByUsername(@Param("username") String username);

}
