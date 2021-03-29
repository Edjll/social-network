package ru.edjll.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.MessageDto;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.service.MessageService;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(
            @RequestParam String senderId,
            @RequestParam String recipientId,
            @AuthenticationPrincipal Principal principal
    ) {
        return messageService.getAllMessageDtoBetweenUsersById(senderId, recipientId, principal);
    }

    @PostMapping("/save")
    public MessageDto save(
            @RequestBody Message message,
            @AuthenticationPrincipal Principal principal
    ) {
        return messageService.save(message, principal);
    }

    @PutMapping("/edit")
    public MessageDto edit(
            @RequestBody Message message,
            @AuthenticationPrincipal Principal principal
    ) {
        return messageService.edit(message, principal);
    }

    @DeleteMapping("/delete")
    public void delete(
            @RequestBody Message message,
            @AuthenticationPrincipal Principal principal
    ) {
        messageService.delete(message, principal);
    }
}
