package com.sparta.first_project.controller;

import com.sparta.first_project.dto.CommentRequestDto;
import com.sparta.first_project.dto.CommentResponseDto;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponseDto create(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.create(id, commentRequestDto, userDetails.getUser());
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,@RequestBody CommentRequestDto commentRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.update(id,commentRequestDto,userDetails.getUser());
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.delete(id, userDetails.getUser());
    }
}
