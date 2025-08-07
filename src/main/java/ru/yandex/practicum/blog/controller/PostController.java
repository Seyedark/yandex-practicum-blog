package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
        return "postsView";
    }

    @GetMapping("/post/add")
    public String addPost() {
        return "addOrEditPost";
    }


    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostWithCommentsById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/post/{id}/edit")
    public String editPost(@PathVariable("id") Long id,
                           Model model) {
        Post post = postService.getPostByIdForEdit(id);
        model.addAttribute("post", post);
        return "addOrEditPost";
    }

    @PostMapping("/post/save")
    public String savePost(@ModelAttribute Post post,
                           @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        postService.savePost(post, imageFile);
        return "redirect:/posts";
    }

    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @PostMapping("/post/{id}/like")
    public String changePostLike(@PathVariable("id") Long id,
                                 @RequestParam("like") Boolean like) {
        postService.changePostLike(id, like);
        return "redirect:/post/" + id;
    }
}