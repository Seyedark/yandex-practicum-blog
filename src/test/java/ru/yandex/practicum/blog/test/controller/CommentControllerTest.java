package ru.yandex.practicum.blog.test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.blog.dao.CommentRepository;
import ru.yandex.practicum.blog.test.config.CommentControllerConfig;
import ru.yandex.practicum.blog.test.config.WebConfiguration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = {CommentControllerConfig.class, WebConfiguration.class})
@WebAppConfiguration
@DisplayName("Класс для проверки взаимодействия контроллером комментариев")
public class CommentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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