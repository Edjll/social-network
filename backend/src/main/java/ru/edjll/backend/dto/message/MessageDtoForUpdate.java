package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.MessageRepository;
import ru.edjll.backend.repository.UserRepository;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoForUpdate {

    @NotNull(message = "{message.id.notNull}")
    @Positive(message = "{message.id.positive}")
    @Exists(table = "message", column = "id", message = "{message.id.exists}")
    private Long id;

    @NotEmpty(message = "{message.senderId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{message.senderId.exists}")
    private String senderId;

    @NotEmpty(message = "{message.recipientId.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{message.recipientId.exists}")
    private String recipientId;

    @NotEmpty(message = "{message.text.notEmpty}")
    private String text;

    @NotNull(message = "{message.createdDate.notNull}")
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate = LocalDateTime.now();

    public MessageDtoForUpdate(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.recipientId = message.getRecipient().getId();
        this.text = message.getText();
        this.createdDate = message.getCreatedDate();
        this.modifiedDate = message.getModifiedDate();
    }

    public Message toMessage() {
        Message message = new Message();
        User sender = new User();
        User recipient = new User();

        sender.setId(senderId);
        recipient.setId(recipientId);

        message.setId(id);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setText(text);
        message.setCreatedDate(createdDate);
        message.setModifiedDate(modifiedDate);

        return message;
    }
}
