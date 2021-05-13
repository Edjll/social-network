package ru.edjll.backend.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String creatorId;
    private String name;
    private String address;
    private PostType type;

    public PostDto(Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate, String creatorId, String name, String address, String type) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.creatorId = creatorId;
        this.name = name;
        this.address = address;
        this.type = PostType.valueOf(type);
    }

    public PostDto(Long id, String text, LocalDateTime createdDate, LocalDateTime modifiedDate, String creatorId, String name, String address) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.creatorId = creatorId;
        this.name = name;
        this.address = address;
    }
}
