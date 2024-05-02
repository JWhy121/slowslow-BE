package com.hani.examinfo.board;

import com.hani.examinfo.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String boardTitle;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 양방향 연관관계 매핑
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setBoard(this);
    }

    public void updateBoard(String boardTitle) {
        this.boardTitle = boardTitle;
    }


}
