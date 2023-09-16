package com.sparta.first_project.repository;

import com.sparta.first_project.entity.Comment;
import com.sparta.first_project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByModifiedAt(Post post);

    List<Comment> findAllByPost(Post post);
}