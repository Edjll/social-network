package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {

    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private MessageUserInfoDto sender;
    private MessageUserInfoDto recipient;

    public MessageDto(
            Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate,
            String senderId, String senderFirstName, String senderLastName, String senderUsername,
            String recipientId, String recipientFirstName, String recipientLastName, String recipientUsername
    ) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.sender = new MessageUserInfoDto(senderId, senderFirstName, senderLastName, senderUsername);
        this.recipient = new MessageUserInfoDto(recipientId, recipientFirstName, recipientLastName, recipientUsername);
    }

    public MessageDto(Message message) {
        this.id = message.getId();
        this.text = message.getText();
        this.createdDate = message.getCreatedDate();
        this.modifiedDate = message.getModifiedDate();
        this.sender = new MessageUserInfoDto(message.getSender());
        this.recipient = new MessageUserInfoDto(message.getRecipient());
    }
}
