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
import ru.edjll.backend.repository.MessageRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
    }

    public MessageDto save(MessageDtoForSave messageDtoForSave) {
        Message savedMessage = messageRepository.save(messageDtoForSave.toMessage());
        return new MessageDto(savedMessage);
    }

    public MessageDto update(MessageDtoForUpdate messageDtoForUpdate) {
        Message savedMessage = messageRepository.save(messageDtoForUpdate.toMessage());
        return new MessageDto(savedMessage);
    }

    public void delete(MessageDtoForDelete messageDtoForDelete) {
        messageRepository.deleteById(messageDtoForDelete.getId());
    }

    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(String senderId, String recipientId) {
        return messageRepository.getAllMessageDtoBetweenUsersById(senderId, recipientId);
    }

    public MessageDto getMessageDtoById(Long id) {
        return messageRepository.getMessageDtoById(id);
    }
}
