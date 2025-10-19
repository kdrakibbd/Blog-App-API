package com.rakib.blog.payloads;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Integer id;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}