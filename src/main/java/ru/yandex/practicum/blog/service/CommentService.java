package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dao.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void addComment(String text, Long postId) {
        commentRepository.addComment(text, postId);
    }

    public void editComment(Long id, String text) {
        commentRepository.editComment(id, text);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteComment(id);
    }
}