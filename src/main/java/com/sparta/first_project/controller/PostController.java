package com.sparta.first_project.controller;

import com.sparta.first_project.dto.BaseResponse;
import com.sparta.first_project.dto.PostRequestDto;
import com.sparta.first_project.dto.PostResponseDto;
import com.sparta.first_project.dto.SuccessResponse;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;




    // 전체 조회
    @Operation(summary = "게시물 전체 조회")
    @GetMapping("/post")
    public ResponseEntity<BaseResponse> findAllPosts() {

        List<PostResponseDto> postResponseDtos = postService.findAllPosts();
        return ResponseEntity.ok().body(new SuccessResponse("전체 게시물 조회 성공", postResponseDtos));
    }

    // 단일 조회
    @Operation(summary = "게시물 단일 조회")
    @GetMapping("post/{id}")
    public ResponseEntity<BaseResponse> findPostById(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.findPostById(id);
        return ResponseEntity.ok()
                .body(new SuccessResponse("게시물 조회 성공 Post ID: " + id, postResponseDto));
    }

    // 생성
    @Operation(summary = "게시물 생성")
    @PostMapping("/post")
    public ResponseEntity<BaseResponse> createPost(
            @RequestBody @Valid PostRequestDto postRequestDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);
        // 게시글에 작성자 이름 추가
        PostResponseDto postResponseDto = postService.createPost(postRequestDto,
                userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("게시물 생성 성공", postResponseDto));
    }

    // 수정
    @Operation(summary = "게시물 수정")
    @PutMapping("post/{id}")
    public ResponseEntity<BaseResponse> updatePost(@PathVariable Long id,
                                                   @RequestBody @Valid PostRequestDto postRequestDto, BindingResult bindingResult,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        PostResponseDto postResponseDto = postService.updatePost(id, postRequestDto,
                userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("게시물 수정 성공", postResponseDto));
    }

    // 삭제
    @Operation(summary = "게시물 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long deletedPostId = postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok()
                .body(new SuccessResponse("게시물 삭제 성공 Post ID: " + deletedPostId));
    }

    // 게시글 좋아요
    @Operation(summary = "게시물 좋아요")
    @PostMapping("post/{id}/likes")
    public ResponseEntity<BaseResponse> likePost(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String responseMessage = postService.likePostToggle(id, userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse(
                responseMessage + " 게시물 id: " + id + " 유저 id: " + userDetails.getUser().getId()));
    }


    private static void checkParamValidation(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}