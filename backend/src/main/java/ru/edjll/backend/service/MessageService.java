package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.message.*;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.MessageRepository;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final String MESSAGE_QUEUE_URL = "/queue/messages";

    public MessageService(MessageRepository messageRepository, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    public void save(String userId, Principal principal, MessageDtoForSave messageDtoForSave) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "user", userId, "exists"));

        Message message = messageDtoForSave.toMessage();
        User sender = new User();
        sender.setId(principal.getName());

        message.setRecipient(user);
        message.setSender(sender);

        Message savedMessage = messageRepository.save(message);

        MessageNotification notification = new MessageNotification(savedMessage.getId(), MessageNotificationAction.CREATED, principal.getName(), savedMessage.getRecipient().getId());

        if (!principal.getName().equals(notification.getRecipientId())) {
            messagingTemplate.convertAndSendToUser(notification.getRecipientId(), MESSAGE_QUEUE_URL, notification);
        }
        messagingTemplate.convertAndSendToUser(principal.getName(), MESSAGE_QUEUE_URL, notification);
    }

    public void update(Long id, Principal principal, MessageDtoForUpdate messageDtoForUpdate) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!message.getSender().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        message.setText(messageDtoForUpdate.getText());
        message.setModifiedDate(messageDtoForUpdate.getModifiedDate());

        Message savedMessage = messageRepository.save(message);
        MessageNotification notification = new MessageNotification(savedMessage.getId(), MessageNotificationAction.UPDATED, principal.getName(), savedMessage.getRecipient().getId());

        if (!principal.getName().equals(notification.getRecipientId())) {
            messagingTemplate.convertAndSendToUser(notification.getRecipientId(), MESSAGE_QUEUE_URL, notification);
        }
        messagingTemplate.convertAndSendToUser(principal.getName(), MESSAGE_QUEUE_URL, notification);
    }

    public void delete(Long id, Principal principal) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!message.getSender().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        messageRepository.deleteById(id);

        MessageNotification notification = new MessageNotification(id, MessageNotificationAction.DELETED, principal.getName(), message.getRecipient().getId());

        if (!principal.getName().equals(notification.getRecipientId())) {
            messagingTemplate.convertAndSendToUser(notification.getRecipientId(), MESSAGE_QUEUE_URL, notification);
        }
        messagingTemplate.convertAndSendToUser(principal.getName(), MESSAGE_QUEUE_URL, notification);
    }

    public List<MessageDto> getAllMessageDtoBetweenUsersById(String senderId, Integer page, Integer size, Principal principal) {
        return messageRepository.getAllMessageDtoBetweenUsersById(senderId, principal.getName(), PageRequest.of(page, size));
    }

    public MessageDto getMessageDtoById(Long id, Principal principal) {
        MessageDto messageDto = messageRepository.getMessageDtoById(id);
        if (!messageDto.getSender().getId().equals(principal.getName()) && !messageDto.getRecipient().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "id", principal.getName(), "forbidden");
        }
        return messageDto;
    }

    public void updateViewed(String userId, Principal principal) {
        messageRepository.updateViewed(userId, principal.getName());
    }

    public void updateViewed(Long id, Principal principal) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!message.getRecipient().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        messageRepository.updateViewed(id);
    }
}
