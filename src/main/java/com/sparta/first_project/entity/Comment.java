package com.sparta.first_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.first_project.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    // 다대일 연관관계 주입 (외래키의 주인)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.username = requestDto.getUsername();
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
