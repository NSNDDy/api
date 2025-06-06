package org.example.demojwt.info.service;

import org.example.demojwt.info.dto.PostRequest;
import org.example.demojwt.info.entity.Post;
import org.example.demojwt.info.entity.User;
import org.example.demojwt.info.repository.PostRepository;
import org.example.demojwt.info.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public Post createPost(Authentication authentication, PostRequest postRequest){
        String username = authentication.getName();
        List<User> user = userRepository.findAllByUsernameContaining(username);

        Post post = Post.builder().title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(user)
                .build();

        return postRepository.save(post);
    }
}
