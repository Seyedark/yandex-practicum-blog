package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Post;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long getPostsCountByTag(String tag) {
        String sql = """
                SELECT count(*) FROM posts p 
                """ + (tag != null && !tag.isEmpty() ? "WHERE ? = ANY(p.tags)" : "");

        return tag != null && !tag.isEmpty()
                ? jdbcTemplate.queryForObject(sql, new Object[]{tag}, Long.class)
                : jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public List<Post> getAllPostsByPages(int page, int size, String tag) {
        int offset = (page - 1) * size;

        String sql = """
                SELECT p.id,
                p.title,
                CASE
                WHEN length(content) > 300 OR array_length(string_to_array(content, E'\\n'), 1) > 3 THEN
                    substring(
                        array_to_string(
                            (string_to_array(content, E'\\n'))[1:3],
                            E'\\n'
                        ),
                        1,
                        300
                    ) || '...'
                ELSE content
                END AS content,
                p.image_path,
                p.likes, p.tags,
                COUNT(c.id) as comment_count
                FROM posts p
                LEFT JOIN comments c ON p.id = c.post_id
                """ + (tag != null && !tag.isEmpty() ? "WHERE ? = ANY(p.tags) " : "") + """
                GROUP BY p.id
                ORDER BY p.id ASC
                LIMIT ? OFFSET ?
                """;

        return jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    int paramIndex = 1;
                    if (tag != null && !tag.isEmpty()) {
                        ps.setString(paramIndex++, tag);
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
                    Array tagsArray = rs.getArray("tags");
                    post.setTags(tagsArray != null ?
                            Arrays.asList((String[]) tagsArray.getArray()) :
                            Collections.emptyList());

                    post.setCommentCount(rs.getInt("comment_count"));
                    return post;
                }
        );
    }
}