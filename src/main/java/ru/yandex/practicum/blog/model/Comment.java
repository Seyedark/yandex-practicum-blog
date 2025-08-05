package ru.yandex.practicum.blog.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private String text;
}