package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDtoForSave {

    @Length(min = 1, max = 150)
    private String text;

    private LocalDateTime createdDate = LocalDateTime.now();

    private Boolean viewed = false;

    public MessageDtoForSave(Message message) {
        this.text = message.getText();
        this.createdDate = message.getCreatedDate();
    }

    public Message toMessage() {
        Message message = new Message();

        message.setText(text);
        message.setCreatedDate(createdDate);
        message.setViewed(viewed);

        return message;
    }
}
