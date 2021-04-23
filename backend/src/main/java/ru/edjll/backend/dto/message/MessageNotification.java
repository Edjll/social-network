package ru.edjll.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageNotification {

    private Long id;
    private MessageNotificationAction action;
    private String senderId;
    private String recipientId;
}
