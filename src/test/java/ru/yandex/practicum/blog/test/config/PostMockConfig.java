package ru.yandex.practicum.blog.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.dao.PostRepository;
import ru.yandex.practicum.blog.dao.PostRepositoryImpl;
import ru.yandex.practicum.blog.service.FileService;
import ru.yandex.practicum.blog.service.PostService;

import static org.mockito.Mockito.mock;

@Configuration
public class PostMockConfig {
    @Bean
    public PostService postService(PostRepository postRepository, FileService fileService) {
        return new PostService(postRepository, fileService);
    }

    @Bean
    public PostRepository postRepository() {
        return mock(PostRepositoryImpl.class);
    }

    @Bean
    public FileService fileService() {
        return mock(FileService.class);
    }
}