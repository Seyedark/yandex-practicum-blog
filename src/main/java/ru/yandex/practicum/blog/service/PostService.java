package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.dao.PostRepository;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    public List<Post> getAllPostsByPages(int page, int size, String tag) {
        return postRepository.getAllPostsByPages(page, size, tag);
    }

    public Post getPostWithCommentsById(Long id) {
        return postRepository.getPostWithCommentsById(id);
    }

    public Post getPostByIdForEdit(Long id) {
        return postRepository.getPostByIdForEditOrDelete(id);
    }

    public Long getPostsCountByTag(String tag) {
        return postRepository.getPostsCountByTag(tag);
    }

    public void savePost(Post post, MultipartFile imageFile) {
        String newImage = fileService.saveFileOnMachine(imageFile);
        if (post.getId() != null && post.getImagePath() != null) {
            fileService.deleteFileOnMachine(post.getImagePath());
        }
        post.setImagePath(newImage);
        post.setTags(formatTagsIfExist(post.getTags()));
        postRepository.savePost(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.getPostByIdForEditOrDelete(id);
        if (post != null) {
            fileService.deleteFileOnMachine(post.getImagePath());
            postRepository.deletePostAndCommentsById(id);
        }
    }

    public void changePostLike(Long id, Boolean like) {
        postRepository.changePostLike(id, like);
    }

    private String formatTagsIfExist(String tags) {
        if (tags == null || tags.trim().isEmpty()) {
            return null;
        } else {
            return tags.replaceAll(",\\s+", ",");
        }
    }
}