package ru.yandex.practicum.blog.model;

import lombok.Data;

import java.util.List;

@Data
public class Post {
    private Long id;
    private String title;
    private String content;
    private String imagePath;
    private int likes;
    private int commentCount;
    private List<String> tags;
}