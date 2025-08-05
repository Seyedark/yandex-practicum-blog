package ru.yandex.practicum.blog.dao;

import ru.yandex.practicum.blog.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> getAllPostsByPages(int page, int size, String tag);

    Long getPostsCountByTag(String tag);

    void savePost(Post post);

    Post getPostWithCommentsById(Long id);

    Post getPostByIdForEditOrDelete(Long id);

    void deletePostAndCommentsById(Long id);

    void changePostLike(Long id, Boolean like);
}