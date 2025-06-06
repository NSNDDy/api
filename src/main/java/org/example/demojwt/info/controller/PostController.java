package org.example.demojwt.info.controller;

import org.example.demojwt.info.dto.PostReponse;
import org.example.demojwt.info.dto.PostRequest;
import org.example.demojwt.info.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post/create")
    public void createPost(
            @RequestBody
            PostRequest postRequest,
            Authentication authentication) {
        postService.createPost(authentication,
                               postRequest);
    }

    @GetMapping("/post/get")
    public List<PostReponse> getPost(Authentication authentication) {
        return postService.getPost(authentication);
    }
}
