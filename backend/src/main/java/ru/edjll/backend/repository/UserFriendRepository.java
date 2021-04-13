package ru.edjll.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.UserFriendKey;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, UserFriendKey> {

    Optional<UserFriend> findByIdUserIdAndIdFriendId(String userId, String friendId);

    @Modifying
    @Transactional
    @Query(value = "delete from user_friend where (user_id = :userId and friend_id = :friendId) or (user_id = :friendId and friend_id = :userId)", nativeQuery = true)
    void deleteByUserIdAndFriendId(@Param("userId") String userId, @Param("friendId") String friendId);
}
