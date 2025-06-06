package org.example.demojwt.info.controller;

import org.example.demojwt.info.dto.PostRequest;
import org.example.demojwt.info.entity.Post;
import org.example.demojwt.info.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post")
    public Post createPost(
            @RequestBody
            PostRequest postRequest,
            Authentication authentication) {

        return postService.createPost(authentication,
                                      postRequest);
    }
}
