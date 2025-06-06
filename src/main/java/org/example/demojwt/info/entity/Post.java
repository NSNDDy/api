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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;
    private String title;
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Id",
    joinColumns =  @JoinColumn(name = "post_id"), // ID của Post
    inverseJoinColumns = @JoinColumn(name = "user_id") // ID của user
    )
    private List<User> author;
}
