package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Post;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void changePostLike(Long id, Boolean like) {
        StringBuilder sql = new StringBuilder("UPDATE posts SET likes = likes");
        if (like) {
            sql.append("+ 1 WHERE id = ?");
        } else {
            sql.append("- 1 WHERE id = ?");
        }
        jdbcTemplate.update(sql.toString(), id);
    }

    @Override
    public void deletePostAndCommentsById(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Post getPostById(Long id) {
        String sql = """
                SELECT id, title, content, tags, image_path, likes
                FROM posts p
                WHERE p.id = ?
                """;
        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                rs -> {
                    Post post = null;
                    while (rs.next()) {
                        post = new Post();
                        post.setId(rs.getLong("id"));
                        post.setTitle(rs.getString("title"));
                        post.setContent(rs.getString("content"));
                        post.setTags(rs.getString("tags"));
                        post.setImagePath(rs.getString("image_path"));
                        post.setLikes(rs.getInt("likes"));
                    }
                    return post;
                });
    }

    @Override
    public Post getPostWithCommentsById(Long id) {
        String sql = """
                SELECT p.id, p.title, p.content, p.image_path, p.likes, p.tags,
                c.id as comment_id, c.text as text
                FROM posts p
                LEFT JOIN comments c ON p.id = c.post_id
                WHERE p.id = ?
                ORDER BY comment_id ASC
                """;
        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                rs -> {
                    Post post = null;
                    List<Comment> comments = new ArrayList<>();
                    while (rs.next()) {
                        if (post == null) {
                            post = new Post();
                            post.setId(rs.getLong("id"));
                            post.setTitle(rs.getString("title"));
                            post.setContent(rs.getString("content"));
                            post.setImagePath(rs.getString("image_path"));
                            post.setLikes(rs.getInt("likes"));
                            post.setTags(rs.getString("tags"));
                        }
                        long commentId = rs.getLong("comment_id");
                        if (commentId != 0) {
                            Comment c = new Comment();
                            c.setId(commentId);
                            c.setText(rs.getString("text"));
                            comments.add(c);
                        }
                    }
                    if (post != null) {
                        post.setCommentCount(comments.size());
                        post.setCommentList(comments);
                    }
                    return post;
                });
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == null) {
            String sql = "INSERT INTO posts (title, content, image_path, tags) " +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getImagePath(), post.getTags());
        } else {
            String sql = "UPDATE posts SET title = ?, content = ?, image_path = ?, likes = ?, tags = ? WHERE id = ?";
            jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getImagePath(), post.getLikes(), post.getTags(), post.getId());
        }
    }

    @Override
    public Long getPostsCountByTag(String tag) {
        String sql = """
                SELECT count(*) FROM posts p
                """ + (tag != null && !tag.isEmpty() ? "WHERE p.tags LIKE ?" : "");
        if (tag != null && !tag.isEmpty()) {
            String likeTag = "%" + tag + "%";
            return jdbcTemplate.queryForObject(sql, new Object[]{likeTag}, Long.class);
        } else {
            return jdbcTemplate.queryForObject(sql, Long.class);
        }
    }

    @Override
    public List<Post> getAllPostsByPages(int page, int size, String tag) {
        int offset = (page - 1) * size;

        String sql = """
                SELECT p.id,
                p.title,
                p.content,
                p.image_path,
                p.likes, p.tags,
                COUNT(c.id) as comment_count
                FROM posts p
                LEFT JOIN comments c ON p.id = c.post_id
                """ + (tag != null && !tag.isEmpty() ? "WHERE p.tags LIKE ? " : "") + """
                GROUP BY p.id
                ORDER BY p.id ASC
                LIMIT ? OFFSET ?
                """;

        return jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    int paramIndex = 1;
                    if (tag != null && !tag.isEmpty()) {
                        String likeTag = "%" + tag + "%";
                        ps.setString(paramIndex++, likeTag);
                    }
                    ps.setInt(paramIndex++, size);
                    ps.setInt(paramIndex, offset);
                    return ps;
                },
                (rs, rowNum) -> {
                    Post post = new Post();
                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setImagePath(rs.getString("image_path"));
                    post.setLikes(rs.getInt("likes"));
                    post.setTags(rs.getString("tags"));
                    post.setCommentCount(rs.getInt("comment_count"));
                    return post;
                }
        );
    }
}