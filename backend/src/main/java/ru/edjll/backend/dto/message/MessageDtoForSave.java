package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.UserRepository;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoForSave {

    @NotEmpty(message = "{message.senderId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{message.senderId.exist}")
    private String senderId;

    @NotEmpty(message = "{message.recipientId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{message.recipientId.exist}")
    private String recipientId;

    @NotEmpty(message = "{message.text.notEmpty}")
    private String text;

    private LocalDateTime createdDate = LocalDateTime.now();

    public MessageDtoForSave(Message message) {
        this.senderId = message.getSender().getId();
        this.recipientId = message.getRecipient().getId();
        this.text = message.getText();
        this.createdDate = message.getCreatedDate();
    }

    public Message toMessage() {
        Message message = new Message();
        User sender = new User();
        User recipient = new User();

        sender.setId(senderId);
        recipient.setId(recipientId);

        message.setSender(sender);
        message.setRecipient(recipient);
        message.setText(text);
        message.setCreatedDate(createdDate);

        return message;
    }
}
