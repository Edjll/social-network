package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.dto.user.UserFtoForMessage;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("select new ru.edjll.backend.dto.user.UserFtoForMessage(u.id, u.username, u.firstName, u.lastName) " +
            "from User u " +
            "join Message m on (u.id = m.sender.id and m.recipient.id = :id) " +
            "or (u.id = m.recipient.id and m.sender.id = :id) group by u.id, u.username, u.firstName, u.lastName")
    Page<UserFtoForMessage> findAllInterlocutors(@Param("id") String id, Pageable pageable);

    boolean existsAllByIdIn(Collection<String> id);
}
