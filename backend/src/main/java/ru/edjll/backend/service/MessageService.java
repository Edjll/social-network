package ru.edjll.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.MessageDto;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.repository.MessageRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageDto save(Message message, Principal principal) {
        if (message.getSender() == null || !Objects.equals(message.getSender().getId(), principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have rights");
        }
        message.setCreatedDate(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return this.getMessageDtoById(savedMessage.getId());
    }

    public MessageDto edit(Message message, Principal principal) {
        if (message.getSender() == null || !Objects.equals(message.getSender().getId(), principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have rights");
        }
        message.setModifiedDate(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return this.getMessageDtoById(savedMessage.getId());
    }

    public void delete(Message message, Principal principal) {
        if (message.getSender() == null || !Objects.equals(message.getSender().getId(), principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have rights");
        }
        messageRepository.deleteById(message.getId());
    }

    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(String senderId, String recipientId, Principal principal) {
        if (!principal.getName().equals(senderId) && !principal.getName().equals(recipientId)) {
            throw new ResponseStatusException(HttpStatus.MULTI_STATUS, "Don't have rights");
        }
        return messageRepository.getAllMessageDtoBetweenUsersById(senderId, recipientId);
    }

    public MessageDto getMessageDtoById(Long id) {
        return messageRepository.getMessageDtoById(id);
    }
}
