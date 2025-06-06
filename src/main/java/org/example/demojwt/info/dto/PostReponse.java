package org.example.demojwt.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demojwt.info.entity.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReponse {
    private String username;
    private String title;
    private String content;

    public PostReponse(Post post) {
        this.username = post.getAuthor().get(0).getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
