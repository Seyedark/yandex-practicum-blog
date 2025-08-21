package ru.yandex.practicum.blog.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.dao.PostRepository;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.service.FileService;
import ru.yandex.practicum.blog.service.PostService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PostService.class)
@ActiveProfiles("test")
@DisplayName("Класс для проверки взаимодействия с сервисом постов")
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private FileService fileService;


    @Test
    @DisplayName("Проверка вызова метода по изменению лайка")
    void changePostLikeTest() {
        postService.changePostLike(1L, true);

        verify(postRepository, times(1)).changePostLike(1L, true);
    }

    @Test
    @DisplayName("Проверка вызова метода удаления поста")
    void deletePostTest() {
        reset(postRepository);

        Post post = new Post();
        post.setImagePath("Путь к изображению");
        when(postRepository.getPostById(1L)).thenReturn(post);
        postService.deletePost(1L);

        verify(postRepository, times(1)).getPostById(1L);
        verify(fileService, times(1)).deleteFileOnMachine(post.getImagePath());
        verify(postRepository, times(1)).deletePostAndCommentsById(1L);
    }

    @Test
    @DisplayName("Проверка вызова метода сохранения поста")
    void savePostTest() {
        Post post = new Post();
        post.setId(1L);
        post.setImagePath("Старый путь к файлу");
        post.setTags("123, 456");

        String newFilePath = "Новый путь к файлу";
        String newTags = "123,456";

        Post postAfterModifications = new Post();
        postAfterModifications.setId(1L);
        postAfterModifications.setImagePath(newFilePath);
        postAfterModifications.setTags(newTags);

        MultipartFile imageFile = mock(MultipartFile.class);

        when(fileService.saveFileOnMachine(imageFile)).thenReturn(newFilePath);

        postService.savePost(post, imageFile);

        verify(fileService, times(1)).saveFileOnMachine(imageFile);
        verify(postRepository, times(1)).savePost(postAfterModifications);
    }

    @Test
    @DisplayName("Проверка вызова метода получения кол-ва постов по тегу")
    void getPostsCountByTagTest() {
        String tag = "Тег";
        when(postRepository.getPostsCountByTag(tag)).thenReturn(1L);

        postService.getPostsCountByTag(tag);

        verify(postRepository, times(1)).getPostsCountByTag(tag);
    }

    @Test
    @DisplayName("Проверка вызова метода получения кол-ва постов по страницам")
    void getAllPostsByPagesTest() {
        Post post = new Post();
        String content = "Это первый абзац.\n" +
                "Это второй абзац.\n" +
                "Это третий абзац.\n" +
                "Это четвёртый абзац.";
        post.setContent(content);
        String contentAfterFormat = "Это первый абзац.\n" +
                "Это второй абзац.\n" +
                "Это третий абзац....";
        when(postRepository.getAllPostsByPages(1, 1, "Тег"))
                .thenReturn(List.of(post));
        postService.getAllPostsByPages(1, 1, "Тег");

        verify(postRepository, times(1)).getAllPostsByPages(1, 1, "Тег");
        assertEquals(post.getContent(), contentAfterFormat);
    }

    @Test
    @DisplayName("Проверка вызова метода получения поста для редактирования")
    void getPostByIdForEditTest() {
        reset(postRepository);
        when(postRepository.getPostById(1L)).thenReturn(new Post());

        postService.getPostByIdForEdit(1L);

        verify(postRepository, times(1)).getPostById(1L);
    }

    @Test
    @DisplayName("Проверка вызова метода получения поста с комментариями")
    void getPostWithCommentsByIdTest() {
        when(postRepository.getPostWithCommentsById(1L)).thenReturn(new Post());

        postService.getPostWithCommentsById(1L);

        verify(postRepository, times(1)).getPostWithCommentsById(1L);
    }
}