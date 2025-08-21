package ru.yandex.practicum.blog.test.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.blog.dao.CommentRepository;
import ru.yandex.practicum.blog.dao.CommentRepositoryImpl;
import ru.yandex.practicum.blog.model.Comment;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@ActiveProfiles("test")
@Import(CommentRepositoryImpl.class)
@DisplayName("Класс для проверки взаимодействия с репозиторием комментариев")
public class CommentRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("ALTER TABLE posts ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("INSERT INTO posts (title, content) VALUES ('Наименование', 'Контент')");
        jdbcTemplate.execute("INSERT INTO comments (post_id, text) VALUES (1, 'Какой-то комментарий')");
    }

    private Comment getCommentById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT text FROM comments WHERE id = ?",
                    new Object[]{id},
                    (rs, rowNum) -> {
                        Comment comment = new Comment();
                        comment.setText(rs.getString("text"));
                        return comment;
                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Test
    @DisplayName("Проверка добавления комментария")
    void saveCommentToDbTest() {
        String text = "Новый комментарий";
        commentRepository.addComment(text, 1L);

        Comment comment = getCommentById(2L);

        assertNotNull(comment);
        assertEquals(text, comment.getText());

    }

    @Test
    @DisplayName("Проверка редактирования комментария")
    void editCommentTest() {
        String text = "Новый комментарий";
        commentRepository.editComment(1L, "Новый комментарий");

        Comment comment = getCommentById(1L);

        assertNotNull(comment);
        assertEquals(text, comment.getText());

    }

    @Test
    @DisplayName("Проверка удаления комментария")
    void deleteCommentTest() {
        commentRepository.deleteComment(1L);

        Comment comment = getCommentById(1L);
        assertNull(comment);
    }
}