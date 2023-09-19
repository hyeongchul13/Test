package com.sparta.first_project.entity;

import com.sparta.first_project.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String author;

    // 연관관계 주입
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Likes> likesList = new ArrayList<>();

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.url = requestDto.getUrl();
        this.author = requestDto.getAuthor();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        comment.setPost(this);
    }
}