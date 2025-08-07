package ru.yandex.practicum.blog.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.controller.CommentController;
import ru.yandex.practicum.blog.service.CommentService;


@Configuration
public class CommentControllerConfig {
    @Bean
    public CommentController commentController(CommentService commentService) {
        return new CommentController(commentService);
    }
}