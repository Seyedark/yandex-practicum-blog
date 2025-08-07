package ru.yandex.practicum.blog.test.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.dao.PostRepository;
import ru.yandex.practicum.blog.dao.PostRepositoryImpl;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.test.config.DataSourceConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, PostRepositoryImpl.class})
@TestPropertySource(locations = "classpath:test-application.properties")
@DisplayName("Класс для проверки взаимодействия с репозиторием постов")
public class PostRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("ALTER TABLE posts ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("INSERT INTO posts (title, content, image_path, tags) VALUES ('Наименование', 'Контент', 'Путь к изображению', 'Тег1')");
        jdbcTemplate.execute("INSERT INTO posts (title, content, image_path, tags) VALUES ('Наименование', 'Контент', 'Путь к изображению', 'Тег2')");
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
    @DisplayName("Проверка увеличения лайка")
    void increaseLikeTest() {
        postRepository.changePostLike(1L, true);
        Post post = postRepository.getPostById(1L);

        assertNotNull(post);
        assertEquals(1, post.getLikes());

    }

    @Test
    @DisplayName("Проверка уменьшения лайка")
    void decreaseLikeTest() {
        postRepository.changePostLike(1L, false);
        Post post = postRepository.getPostById(1L);

        assertNotNull(post);
        assertEquals(-1, post.getLikes());
    }

    @Test
    @DisplayName("Проверка удаления поста и связанных с ним комментариев")
    void deletePostTest() {
        postRepository.deletePostAndCommentsById(1L);

        Post post = postRepository.getPostById(1L);
        Comment comment = getCommentById(1L);

        assertNull(post);
        assertNull(comment);
    }

    @Test
    @DisplayName("Проверка получения поста для редактирования или удаления")
    void getPostByIdForEditOrDeleteTest() {
        Post post = postRepository.getPostById(1L);

        assertNotNull(post);
    }

    @Test
    @DisplayName("Проверка получения поста c комментариями")
    void getPostWithCommentsByIdTest() {
        Post post = postRepository.getPostWithCommentsById(1L);

        assertNotNull(post);
        assertNotNull(post.getCommentList());
        assertEquals(1, post.getCommentList().size());
    }

    @Test
    @DisplayName("Проверка сохранения нового поста")
    void saveNewPostTest() {
        Post post = new Post();
        String title = "Новый пост";
        String content = "Новый контент";
        post.setTitle(title);
        post.setContent(content);
        postRepository.savePost(post);

        Post postAfterSave = postRepository.getPostById(3L);

        assertNotNull(postAfterSave);
        assertEquals(title, postAfterSave.getTitle());
    }

    @Test
    @DisplayName("Проверка сохранения отредактированного поста")
    void saveEditPostTest() {
        String newTitle = "Новый пост";
        String newContent = "Новый контент";
        String newImagePath = "Новый путь к изображению";
        String newTags = "Новый тег";
        int newLikes = 5;

        Post post = new Post();
        post.setTitle(newTitle);
        post.setContent(newContent);
        post.setImagePath(newImagePath);
        post.setTags(newTags);
        post.setLikes(newLikes);
        post.setId(1L);
        postRepository.savePost(post);

        Post postAfterSave = postRepository.getPostById(1L);

        assertNotNull(postAfterSave);
        assertEquals(newTitle, postAfterSave.getTitle());
        assertEquals(newContent, postAfterSave.getContent());
        assertEquals(newImagePath, postAfterSave.getImagePath());
        assertEquals(newTags, postAfterSave.getTags());
        assertEquals(newLikes, postAfterSave.getLikes());
    }

    @Test
    @DisplayName("Проверка метода поиска кол-ва записей по тегу")
    void getPostsCountByTagTest() {
        String tag = "Тег";
        Long count = postRepository.getPostsCountByTag(tag);

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Проверка метода поиска кол-ва записей по тегу")
    void getAllPostsByPagesTest() {
        String tag = "Тег";
        List<Post> postsWithOneElementPageSize = postRepository.getAllPostsByPages(1, 1, tag);
        List<Post> postsWithTwoElementPageSize = postRepository.getAllPostsByPages(1, 2, tag);

        assertNotNull(postsWithOneElementPageSize);
        assertEquals(1, postsWithOneElementPageSize.size());
        assertNotNull(postsWithTwoElementPageSize);
        assertEquals(2, postsWithTwoElementPageSize.size());
    }
}