package ru.yandex.practicum.blog.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class Post {
    private Long id;
    @NotBlank(message = "Название поста не может быть пустым!")
    private String title;
    @NotBlank(message = "Текст поста не может быть пустым!")
    private String content;
    private String imagePath;
    private int likes;
    private int commentCount;
    private String tags;
    private List<Comment> commentList;
}