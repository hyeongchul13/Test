package com.sparta.first_project.controller;

import com.sparta.first_project.dto.BaseResponse;
import com.sparta.first_project.dto.CommentRequestDto;
import com.sparta.first_project.dto.SuccessResponse;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{postid}")
    public ResponseEntity<BaseResponse> createComment(@PathVariable Long postid,
                                                      @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 생성 성공",
                        commentService.createComment(postid, requestDto, userDetails.getUser())));
    }

    @PutMapping("/comments/{postid}")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable Long postid,
                                                      @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal
                                                      UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 수정 성공",
                        commentService.updateComment(postid, requestDto, userDetails.getUser())));
    }

    @DeleteMapping("/comments/{postid}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable Long postid,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(postid, userDetails.getUser());

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 삭제 성공 + Comment Id: " + postid));
    }

    private static void checkParamValidation(BindingResult bindingResult) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}
