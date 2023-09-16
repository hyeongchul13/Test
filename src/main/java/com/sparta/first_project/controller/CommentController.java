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
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}/comment")
    public ResponseEntity<BaseResponse> createComment(@PathVariable Long id,
                                                      @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 생성 성공",
                        commentService.createComment(id, requestDto, userDetails.getUser())));
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable Long id,
                                                      @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal
                                                      UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 수정 성공",
                        commentService.updateComment(id, requestDto, userDetails.getUser())));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(id, userDetails.getUser());

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 삭제 성공 + Comment Id: " + id));
    }

    private static void checkParamValidation(BindingResult bindingResult) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}