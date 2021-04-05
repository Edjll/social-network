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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoForDelete {

    @NotNull(message = "{message.id.notNull}")
    @Positive(message = "{message.id.positive}")
    @Exists(typeRepository = MessageRepository.class, message = "{message.id.exists}")
    private Long id;

    @NotEmpty(message = "{message.senderId.notEmpty}")
    @Exists(typeRepository = UserRepository.class, message = "{message.senderId.exists}")
    private String senderId;

    public MessageDtoForDelete(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
    }

    public Message toMessage() {
        Message message = new Message();
        User sender = new User();

        sender.setId(senderId);
        message.setSender(sender);

        return message;
    }
}
