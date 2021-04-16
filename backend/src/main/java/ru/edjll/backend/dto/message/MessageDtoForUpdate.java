package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoForUpdate {

    @NotEmpty(message = "{message.text.notEmpty}")
    private String text;

    private LocalDateTime modifiedDate = LocalDateTime.now();

    public Message toMessage() {
        Message message = new Message();

        message.setText(text);
        message.setModifiedDate(modifiedDate);

        return message;
    }
}
