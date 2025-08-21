package ru.yandex.practicum.blog.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.blog.dao.CommentRepository;
import ru.yandex.practicum.blog.service.CommentService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CommentService.class)
@DisplayName("Класс для проверки взаимодействия с сервисом комментариев")
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Проверка вызова метода по добавлению комментария")
    void addCommentTest() {
        commentService.addComment("Текст", 1L);

        verify(commentRepository, times(1)).addComment("Текст", 1L);
    }

    @Test
    @DisplayName("Проверка вызова метода по редактирования комментария")
    void editCommentTest() {
        commentService.editComment(1L, "Текст");

        verify(commentRepository, times(1)).editComment(1L, "Текст");
    }

    @Test
    @DisplayName("Проверка вызова метода по удалению комментария")
    void deleteCommentTest() {
        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).deleteComment(1L);
    }
}