package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.entity.Message;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoForUpdate {

    @Length(min = 1, max = 150)
    private String text;

    private LocalDateTime modifiedDate = LocalDateTime.now();

    public Message toMessage() {
        Message message = new Message();

        message.setText(text);
        message.setModifiedDate(modifiedDate);

        return message;
    }
}
