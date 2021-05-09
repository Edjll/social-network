package ru.edjll.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.user.friend.UserFriendStatusDto;
import ru.edjll.backend.dto.user.info.UserInfoDtoForFriendsPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForUserCart;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.UserFriendKey;
import ru.edjll.backend.entity.UserFriendStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, UserFriendKey> {

    @Query(nativeQuery = true)
    List<UserInfoDtoForFriendsPage> getAllFriendsDto(
            @Param("user_id") String userId,
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("city_id") Long cityId,
            @Param("country_id") Long countryId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<UserInfoDtoForUserCart> getAllFriendCardsDto(
            @Param("user_id") String userId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<UserInfoDtoForFriendsPage> getAllSubscribersDto(
            @Param("user_id") String userId,
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("city_id") Long cityId,
            @Param("country_id") Long countryId,
            Pageable pageable
    );

    @Query(nativeQuery = true)
    List<UserInfoDtoForUserCart> getAllSubscriberCardsDto(
            @Param("user_id") String userId,
            Pageable pageable
    );

    @Query("select new ru.edjll.backend.dto.user.friend.UserFriendStatusDto(uf.status, uf.id.friend.id) " +
            "from UserFriend uf " +
            "where uf.id.user.id = :user_id and uf.id.friend.id = :friend_id " +
            "or uf.id.friend.id = :user_id and uf.id.user.id = :friend_id ")
    UserFriendStatusDto friendshipExists(@Param("user_id") String userId, @Param("friend_id") String friendId);

    Integer countByIdUserIdAndStatusOrIdFriendIdAndStatus(String userId, UserFriendStatus s1, String friendId, UserFriendStatus s2);

    Integer countByIdUserIdAndStatus(String userId, UserFriendStatus status);

    @Modifying
    @Transactional
    @Query(value = "delete from user_friend where (user_id = :userId and friend_id = :friendId) or (user_id = :friendId and friend_id = :userId)", nativeQuery = true)
    void deleteByUserIdAndFriendId(@Param("userId") String userId, @Param("friendId") String friendId);
}
