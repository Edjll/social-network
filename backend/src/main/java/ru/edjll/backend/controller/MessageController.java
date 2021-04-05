package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.message.MessageDto;
import ru.edjll.backend.dto.message.MessageDtoForDelete;
import ru.edjll.backend.dto.message.MessageDtoForSave;
import ru.edjll.backend.dto.message.MessageDtoForUpdate;
import ru.edjll.backend.repository.UserRepository;
import ru.edjll.backend.service.MessageService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@RestController
@RequestMapping("/message")
@Validated
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize("principal.getClaim('sub') == #senderId")
    @GetMapping
    public Collection<MessageDto> getAllMessageDtoBetweenUsersById(
            @RequestParam
            @NotEmpty(message = "{message.senderId.notEmpty}")
            @Exists(typeRepository = UserRepository.class, message = "{message.senderId.exists}") String senderId,
            @RequestParam
            @NotEmpty(message = "{message.recipientId.notEmpty}")
            @Exists(typeRepository = UserRepository.class, message = "{message.recipientId.exists}") String recipientId
    ) {
        return messageService.getAllMessageDtoBetweenUsersById(senderId, recipientId);
    }

    @PreAuthorize("principal.getClaim('sub') == #messageDtoForSave.senderId")
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto save(@RequestBody @Valid MessageDtoForSave messageDtoForSave) {
        return messageService.save(messageDtoForSave);
    }

    @PreAuthorize("principal.getClaim('sub') == #messageDtoForUpdate.senderId")
    @PutMapping("/update")
    public MessageDto update(@RequestBody @Valid MessageDtoForUpdate messageDtoForUpdate) {
        return messageService.update(messageDtoForUpdate);
    }

    @PreAuthorize("principal.getClaim('sub') == #messageDtoForDelete.senderId or hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public void delete(@RequestBody @Valid MessageDtoForDelete messageDtoForDelete) {
        messageService.delete(messageDtoForDelete);
    }
}
