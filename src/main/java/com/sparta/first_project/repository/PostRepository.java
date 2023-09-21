package com.sparta.first_project.repository;

import com.sparta.first_project.dto.PostResponseDto;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.entity.QComment;
import com.sparta.first_project.entity.QPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    @Query("select p.id, p.title, p.content, p.author, p.createdAt, p.modifiedAt from Post p order by p.createdAt desc")
    Page<Post> findAllByOrderByModifiedAtDesc(Pageable pageable);//페이징 //최신순 정렬

}
