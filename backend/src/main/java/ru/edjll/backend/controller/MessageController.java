package ru.edjll.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.MessageDto;
import ru.edjll.backend.entity.Message;
import ru.edjll.backend.service.MessageService;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<Collection<MessageDto>> getAllMessageDtoBetweenUsersById(
            @RequestParam String senderId,
            @RequestParam String recipientId,
            @AuthenticationPrincipal Principal principal
    ) {
        if (!principal.getName().equals(senderId) && !principal.getName().equals(recipientId)) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(messageService.getAllMessageDtoBetweenUsersById(senderId, recipientId));
    }

    @PostMapping("/save")
    public ResponseEntity<MessageDto> save(@RequestBody Message message, @AuthenticationPrincipal Principal principal) {
        if (message.getSender() == null || !Objects.equals(message.getSender().getId(), principal.getName())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(messageService.save(message));
    }

    @PutMapping("/edit")
    public ResponseEntity<MessageDto> edit(@RequestBody Message message, @AuthenticationPrincipal Principal principal) {
        if (message.getSender() == null || !Objects.equals(message.getSender().getId(), principal.getName())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(messageService.edit(message));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Message message, @AuthenticationPrincipal Principal principal) {
        if (message.getSender() == null || !Objects.equals(message.getSender().getId(), principal.getName())) {
            return ResponseEntity.badRequest().body(null);
        }
        messageService.delete(message);
        return ResponseEntity.ok().body(null);
    }
}
