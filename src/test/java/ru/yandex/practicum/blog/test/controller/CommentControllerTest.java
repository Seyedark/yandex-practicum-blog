package ru.yandex.practicum.blog.test.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.blog.controller.CommentController;
import ru.yandex.practicum.blog.dao.CommentRepository;
import ru.yandex.practicum.blog.service.CommentService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {CommentController.class, CommentService.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Класс для проверки взаимодействия контроллером комментариев")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Проверка интеграции метода редактирования комментария до репозитория")
    void editCommentIntegrationTest() throws Exception {
        mockMvc.perform(post("/comment/1/edit")
                        .param("text", "Комментарий")
                        .param("postId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/2"));

        verify(commentRepository, times(1)).editComment(1L, "Комментарий");
    }

    @Test
    @DisplayName("Проверка интеграции метода удаления комментария до репозитория")
    void deleteCommentIntegrationTest() throws Exception {
        mockMvc.perform(post("/comment/1/delete")
                        .param("postId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/2"));

        verify(commentRepository, times(1)).deleteComment(1L);
    }

    @Test
    @DisplayName("Проверка интеграции метода добавления комментария до репозитория")
    void addCommentIntegrationTest() throws Exception {
        mockMvc.perform(post("/comment/add")
                        .param("text", "Комментарий")
                        .param("postId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/2"));

        verify(commentRepository, times(1)).addComment("Комментарий", 2L);
    }
}