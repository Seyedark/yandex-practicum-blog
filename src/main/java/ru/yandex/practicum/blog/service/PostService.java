package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dao.PostRepository;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> getAllPostsByPages(int page, int size, String tag) {
        return postRepository.getAllPostsByPages(page, size, tag);
    }

    public Long getPostsCountByTag(String tag) {
        return postRepository.getPostsCountByTag(tag);
    }
}