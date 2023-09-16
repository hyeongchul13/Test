package com.sparta.first_project.entity;

import com.sparta.first_project.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content", nullable = false, length = 500)
    private String content;
    @Column(name = "title")
    private String title;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post(PostRequestDto postrequestDto, User user) {
        this.content = postrequestDto.getContent();
        this.title = postrequestDto.getTitle();
        this.user = user;
    }

    public void update(PostRequestDto postrequestDto, User user) {
        this.content = postrequestDto.getContent();
        this.title = postrequestDto.getTitle();
        this.user = user;
    }
}
