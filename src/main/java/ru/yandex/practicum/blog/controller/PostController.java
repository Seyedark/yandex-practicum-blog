package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @RequestMapping("/posts")
    public String getAllPosts(@RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              @RequestParam(name = "tag", required = false) String tag,
                              Model model) {
        Long postsByTag = postService.getPostsCountByTag(tag);
        List<Post> posts = postService.getAllPostsByPages(page, size, tag);

        int totalPages = (int) Math.ceil((double) postsByTag / size);

        model.addAttribute("currentSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/add-post")
    public String getPosts(Model model) {
        return "addPost";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        return "post";
    }
}