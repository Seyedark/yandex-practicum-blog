package ru.yandex.practicum.blog.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.dao.CommentRepository;
import ru.yandex.practicum.blog.dao.CommentRepositoryImpl;
import ru.yandex.practicum.blog.service.CommentService;

import static org.mockito.Mockito.mock;

@Configuration
public class CommentMockConfig {

    @Bean
    public CommentService commentService(CommentRepository commentRepository) {
        return new CommentService(commentRepository);
    }

    @Bean
    public CommentRepository commentRepository() {
        return mock(CommentRepositoryImpl.class);
    }
}