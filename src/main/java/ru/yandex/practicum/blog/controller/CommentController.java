package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.blog.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/add")
    public String addComment(@RequestParam("text") String text,
                             @RequestParam("postId") Long postId) {
        commentService.addComment(text, postId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/{id}/edit")
    public String editComment(@PathVariable("id") Long id,
                              @RequestParam("text") String text,
                              @RequestParam("postId") Long postId) {
        commentService.editComment(id, text);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable("id") Long id,
                                @RequestParam("postId") Long postId) {
        commentService.deleteComment(id);
        return "redirect:/post/" + postId;
    }
}