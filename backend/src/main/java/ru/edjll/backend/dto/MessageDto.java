package ru.edjll.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Message;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageDto {

    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserDto sender;
    private UserDto recipient;

    public MessageDto(
            Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate,
            String senderId, String senderFirstName, String senderLastName, String senderUsername,
            String recipientId, String recipientFirstName, String recipientLastName, String recipientUsername
    ) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.sender = new UserDto(senderId, senderFirstName, senderLastName, senderUsername);
        this.recipient = new UserDto(recipientId, recipientFirstName, recipientLastName, recipientUsername);
    }

    public MessageDto(Message message) {
        this.id = message.getId();
        this.text = message.getText();
        this.createdDate = message.getCreatedDate();
        this.modifiedDate = message.getModifiedDate();
        this.sender = new UserDto(message.getSender());
        this.recipient = new UserDto(message.getRecipient());
    }
}
