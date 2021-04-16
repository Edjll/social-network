package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.message.MessageDto;
import ru.edjll.backend.dto.message.MessageDtoForDelete;
import ru.edjll.backend.dto.message.MessageDtoForSave;
import ru.edjll.backend.dto.message.MessageDtoForUpdate;
import ru.edjll.backend.service.MessageService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Validated
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{userId}/messages")
    @ResponseStatus(HttpStatus.OK)
    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(
            @PathVariable
            @NotEmpty(message = "{message.senderId.notEmpty}")
            @Exists(table = "user_entity", column = "id", message = "{message.senderId.exists}") String userId,
            Principal principal
    ) {
        return messageService.getAllMessageDtoBetweenUsersById(userId, principal);
    }

    @PostMapping("/{userId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto save(
            @PathVariable
            @NotEmpty(message = "{message.senderId.notEmpty}")
            @Exists(table = "user_entity", column = "id", message = "{message.senderId.exists}") String userId,
            Principal principal,
            @RequestBody
            @Valid MessageDtoForSave messageDtoForSave) {
        return messageService.save(userId, principal, messageDtoForSave);
    }

    @PutMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDto update(
            @PathVariable
            @NotNull(message = "{message.id.notNull}")
            @Positive(message = "{message.id.positive}")
            @Exists(table = "message", column = "id", message = "{message.id.exists}") Long id,
            Principal principal,
            @RequestBody
            @Valid MessageDtoForUpdate messageDtoForUpdate) {
        return messageService.update(id, principal, messageDtoForUpdate);
    }

    @DeleteMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            @NotNull(message = "{message.id.notNull}")
            @Positive(message = "{message.id.positive}")
            @Exists(table = "message", column = "id", message = "{message.id.exists}") Long id,
            Principal principal
    ) {
        messageService.delete(id, principal);
    }
}
