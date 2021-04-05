package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Message;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private MessageUserDto sender;
    private MessageUserDto recipient;

    public MessageDto(
            Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate,
            String senderId, String senderFirstName, String senderLastName, String senderUsername,
            String recipientId, String recipientFirstName, String recipientLastName, String recipientUsername
    ) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.sender = new MessageUserDto(senderId, senderFirstName, senderLastName, senderUsername);
        this.recipient = new MessageUserDto(recipientId, recipientFirstName, recipientLastName, recipientUsername);
    }

    public MessageDto(Message message) {
        this.id = message.getId();
        this.text = message.getText();
        this.createdDate = message.getCreatedDate();
        this.modifiedDate = message.getModifiedDate();
        this.sender = new MessageUserDto(message.getSender());
        this.recipient = new MessageUserDto(message.getRecipient());
    }
}
