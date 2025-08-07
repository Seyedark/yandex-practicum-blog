package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addComment(String text, Long postId) {
        String sql = "INSERT INTO comments (text, post_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, text, postId);
    }

    @Override
    public void editComment(Long id, String text) {
        String sql = "UPDATE comments SET text = ? WHERE id = ?";
        jdbcTemplate.update(sql, text, id);
    }

    @Override
    public void deleteComment(Long id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}