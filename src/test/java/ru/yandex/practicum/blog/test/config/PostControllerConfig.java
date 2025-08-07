package ru.yandex.practicum.blog.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.controller.PostController;
import ru.yandex.practicum.blog.service.PostService;

@Configuration
public class PostControllerConfig {
    @Bean
    public PostController postController(PostService postService) {
        return new PostController(postService);
    }
}