package com.sparta.first_project.controller;

import com.sparta.first_project.dto.PostRequestDto;
import com.sparta.first_project.dto.PostResponseDto;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    // 1. 게시글 생성
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    // 2. 게시글 전체 목록 조회
    @GetMapping("/post")
    public List<PostResponseDto> getPostList() {
        return postService.getPostList();
    }

    // 3. 선택한 게시글 조회
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 4. 선택한 게시글 수정
    @PutMapping("/post/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.update(id, requestDto, userDetails.getUser());
    }

    // 5. 선택한 게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.delete(id, userDetails.getUser());
    }
}
