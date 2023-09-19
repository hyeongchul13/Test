package com.sparta.first_project.repository;

import com.sparta.first_project.entity.Comment;
import com.sparta.first_project.entity.Likes;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostAndUser(Post post, User user);

    Optional<Likes> findByCommentAndUser(Comment comment, User user);
}