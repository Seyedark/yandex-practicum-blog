package ru.yandex.practicum.blog.dao;

public interface CommentRepository {
    void addComment(String text, Long postId);

    void editComment(Long id, String text);

    void deleteComment(Long id);
}