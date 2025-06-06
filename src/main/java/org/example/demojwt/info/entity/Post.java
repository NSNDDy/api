package org.example.demojwt.info.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private Long postId;
    private String title;
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Id",
    joinColumns =  @JoinColumn(name = "postId"), // ID của Post
    inverseJoinColumns = @JoinColumn(name = "userId") // ID của user
    )
    private List<User> author;
}
