package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addComment(String text, Long postId) {
        String sql = "INSERT INTO comments (text, post_id) " +
                "VALUES (?, ?) RETURNING id";
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, text);
            ps.setLong(2, postId);
            return ps;
        }, (rs, rowNum) -> rs.getLong("id"));
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