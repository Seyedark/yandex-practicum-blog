package ru.yandex.practicum.blog.test.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.blog.controller.PostController;
import ru.yandex.practicum.blog.dao.PostRepository;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.service.FileService;
import ru.yandex.practicum.blog.service.PostService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {PostController.class, PostService.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Класс для проверки взаимодействия контроллером постов")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private FileService fileService;


    @Test
    @DisplayName("Проверка интеграции метода получения списка постов до репозитория")
    void getAllPostsTest() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTags("Тег");
        post.setContent("Контент");

        when(postRepository.getPostsCountByTag("Tег")).thenReturn(1L);
        when(postRepository.getAllPostsByPages(1, 10, "Тег")).thenReturn(List.of(post));
        mockMvc.perform(get("/posts")
                        .param("page", "1")
                        .param("size", "10")
                        .param("tag", "Тег"))
                .andExpect(status().isOk())
                .andExpect(view().name("postsView"))
                .andExpect(model().attributeExists("currentSize"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    @DisplayName("Проверка метода получения пустой формы редактирования")
    void addPostTest() throws Exception {
        mockMvc.perform(get("/post/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addOrEditPost"));
    }

    @Test
    @DisplayName("Проверка интеграции метода получения поста с комментариями до репозитория")
    void getPostByIdIntegrationTest() throws Exception {
        Post post = new Post();
        post.setId(1L);

        when(postRepository.getPostWithCommentsById(1L)).thenReturn(post);

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));

        verify(postRepository, times(1)).getPostWithCommentsById(1L);
    }

    @Test
    @DisplayName("Проверка интеграции метода редактирования поста до репозитория")
    void editPostIntegrationTest() throws Exception {
        reset(postRepository);
        Post post = new Post();
        post.setId(1L);

        when(postRepository.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/post/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("addOrEditPost"))
                .andExpect(model().attributeExists("post"));

        verify(postRepository, times(1)).getPostById(1L);
    }

    @Test
    @DisplayName("Проверка интеграции метода сохранения поста до репозитория")
    void savePostIntegrationTest() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("image", "", "application/octet-stream", new byte[0]);
        when(fileService.saveFileOnMachine(emptyFile)).thenReturn(null);

        mockMvc.perform(multipart("/post/save")
                        .file(emptyFile)
                        .param("tags", "123, 456")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));


        Post post = new Post();
        post.setImagePath(null);
        post.setTags("123,456");

        verify(postRepository, times(1)).savePost(post);
    }

    @Test
    @DisplayName("Проверка интеграции метода удаления поста до репозитория")
    void deletePostIntegrationTest() throws Exception {
        reset(postRepository);

        Post post = new Post();
        post.setId(1L);
        when(postRepository.getPostById(1L)).thenReturn(post);

        mockMvc.perform(post("/post/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));


        verify(postRepository, times(1)).getPostById(1L);
        verify(fileService, times(1)).deleteFileOnMachine(post.getImagePath());
        verify(postRepository, times(1)).deletePostAndCommentsById(1L);
    }

    @Test
    @DisplayName("Проверка интеграции метода изменения лайка до репозитория")
    void changePostLikeIntegrationTest() throws Exception {
        mockMvc.perform(post("/post/1/like")
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/1"));

        verify(postRepository, times(1)).changePostLike(1L, true);
    }
}