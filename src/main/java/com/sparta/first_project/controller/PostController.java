package com.sparta.first_project.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.first_project.dto.BaseResponse;
import com.sparta.first_project.dto.PostRequestDto;
import com.sparta.first_project.dto.PostResponseDto;
import com.sparta.first_project.dto.SuccessResponse;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @Operation(summary = "게시물 전체 조회",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @GetMapping("/posts")
    public ResponseEntity<BaseResponse> findAllPosts() {

        List<PostResponseDto> postResponseDtos = postService.findAllPosts();
        return ResponseEntity.ok().body(new SuccessResponse("전체 게시물 조회 성공", postResponseDtos));
    }


    // 게시글 전체 조회 - 페이징 - 최신순
    @Operation(summary = "게시글 전체 조회",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GetMapping("/post")
    public Page<Post> getPost(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPost(pageable);
    }


    // 단일 조회
    @Operation(summary = "게시물 단일 조회",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @GetMapping("posts/{postid}")
    public ResponseEntity<BaseResponse> findPostById(@PathVariable Long postid) {
        PostResponseDto postResponseDto = postService.findPostById(postid);
        return ResponseEntity.ok()
                .body(new SuccessResponse("게시물 조회 성공 Post ID: " + postid, postResponseDto));
    }

    // 생성
    @Operation(summary = "게시물 생성",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @PostMapping("/posts")
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
    @Operation(summary = "게시물 수정",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @PutMapping("posts/{postid}")
    public ResponseEntity<BaseResponse> updatePost(@PathVariable Long postid,
                                                   @RequestBody @Valid PostRequestDto postRequestDto, BindingResult bindingResult,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        PostResponseDto postResponseDto = postService.updatePost(postid, postRequestDto,
                userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("게시물 수정 성공", postResponseDto));
    }

    // 삭제
    @Operation(summary = "게시물 삭제",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @DeleteMapping("posts/{postid}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable Long postid,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long deletedPostId = postService.deletePost(postid, userDetails.getUser());
        return ResponseEntity.ok()
                .body(new SuccessResponse("게시물 삭제 성공 Post ID: " + deletedPostId));
    }

    // 게시글 좋아요
    @Operation(summary = "게시물 좋아요",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @PostMapping("posts/{postid}/likes")
    public ResponseEntity<BaseResponse> likePost(@PathVariable Long postid,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String responseMessage = postService.likePostToggle(postid, userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse(
                responseMessage + " 게시물 id: " + postid + " 유저 id: " + userDetails.getUser().getId()));
    }

    private static void checkParamValidation(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}