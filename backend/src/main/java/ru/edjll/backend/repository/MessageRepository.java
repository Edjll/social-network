package ru.edjll.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.edjll.backend.dto.message.MessageDto;
import ru.edjll.backend.entity.Message;

import javax.transaction.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select new ru.edjll.backend.dto.message.MessageDto(" +
                "m.id, m.text, m.createdDate, m.modifiedDate, " +
                "s.id, s.firstName, s.lastName, s.username, " +
                "r.id, r.firstName, r.lastName, r.username) " +
            "from Message m join m.sender s join m.recipient r " +
            "where (m.sender.id = :senderId and m.recipient.id = :recipientId) " +
            "or (m.sender.id = :recipientId and m.recipient.id = :senderId) " +
            "order by m.createdDate desc ")
    List<MessageDto> getAllMessageDtoBetweenUsersById(@Param("senderId") String senderId, @Param("recipientId") String recipientId, Pageable pageable);

    @Query("select new ru.edjll.backend.dto.message.MessageDto(" +
                "m.id, m.text, m.createdDate, m.modifiedDate, " +
                "s.id, s.firstName, s.lastName, s.username, " +
                "r.id, r.firstName, r.lastName, r.username) " +
            "from Message m join m.sender s join m.recipient r " +
            "where m.id = :id ")
    MessageDto getMessageDtoById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update Message m set m.viewed = true where m.sender.id = :senderId and m.recipient.id = :recipientId")
    void updateViewed(@Param("senderId") String userId, @Param("recipientId") String name);

    @Modifying
    @Transactional
    @Query("update Message m set m.viewed = true where m.id = :id")
    void updateViewed(@Param("id") Long id);
}
