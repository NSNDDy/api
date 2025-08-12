//package org.example.demojwt.info.service;
//
//import org.example.demojwt.info.dto.PostReponse;
//import org.example.demojwt.info.dto.PostRequest;
//import org.example.demojwt.info.entity.Post;
//import org.example.demojwt.info.entity.User;
//import org.example.demojwt.info.repository.PostRepository;
//import org.example.demojwt.info.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PostService {
//
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    public void createPost(Authentication authentication, PostRequest postRequest){
//        String username = authentication.getName();
//        List<User> user = userRepository.findAllByUsernameContaining(username);
//        Post post = Post.builder()
//                .title(postRequest.getTitle())
//                .content(postRequest.getContent())
//                .author(user)
//                .build();
//        postRepository.save(post);;
//    }
//
//
//    public List<PostReponse> getPost(Authentication authentication){
//        List<PostReponse> postReponse;
//        List<Post> postList = postRepository.findAll();
//        postReponse = postList.stream().map(PostReponse::new).toList();
//        return postReponse;
//
//    }
//}
