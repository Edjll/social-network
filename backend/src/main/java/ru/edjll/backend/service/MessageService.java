package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.MessageDto;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageDto save(Message message) {
        message.setCreatedDate(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return this.getMessageDtoById(savedMessage.getId());
    }

    public MessageDto edit(Message message) {
        message.setModifiedDate(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return this.getMessageDtoById(savedMessage.getId());
    }

    public void delete(Message message) {
        messageRepository.deleteById(message.getId());
    }

    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(String senderId, String recipientId) {
        return messageRepository.getAllMessageDtoBetweenUsersById(senderId, recipientId);
    }

    public MessageDto getMessageDtoById(Long id) {
        return messageRepository.getMessageDtoById(id);
    }
}
