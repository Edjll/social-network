package ru.edjll.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.message.MessageDto;
import ru.edjll.backend.dto.message.MessageDtoForDelete;
import ru.edjll.backend.dto.message.MessageDtoForSave;
import ru.edjll.backend.dto.message.MessageDtoForUpdate;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.MessageRepository;

import java.security.Principal;
import java.util.Collection;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public MessageDto save(String userId, Principal principal, MessageDtoForSave messageDtoForSave) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "user", userId.toString(), "exists"));

        Message message = messageDtoForSave.toMessage();
        User sender = new User();
        sender.setId(principal.getName());

        message.setRecipient(user);
        message.setSender(sender);

        Message savedMessage = messageRepository.save(message);

        return this.getMessageDtoById(savedMessage.getId());
    }

    public MessageDto update(Long id, Principal principal, MessageDtoForUpdate messageDtoForUpdate) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!message.getSender().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        message.setText(messageDtoForUpdate.getText());
        message.setModifiedDate(messageDtoForUpdate.getModifiedDate());

        Message savedMessage = messageRepository.save(message);
        return this.getMessageDtoById(savedMessage.getId());
    }

    public void delete(Long id, Principal principal) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!message.getSender().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        messageRepository.deleteById(id);
    }

    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(String senderId, Principal principal) {
        return messageRepository.getAllMessageDtoBetweenUsersById(senderId, principal.getName());
    }

    public MessageDto getMessageDtoById(Long id) {
        return messageRepository.getMessageDtoById(id);
    }
}
