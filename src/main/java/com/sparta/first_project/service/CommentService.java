package com.sparta.first_project.service;


import com.sparta.first_project.dto.CommentRequestDto;
import com.sparta.first_project.dto.CommentResponseDto;
import com.sparta.first_project.entity.Comment;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.entity.UserRoleEnum;
import com.sparta.first_project.repository.CommentRepository;
import com.sparta.first_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentResponseDto create(Long id, CommentRequestDto commentRequestDto, User user) {
        Post post = findPost(id);
        Comment comment = commentRepository.save(new Comment(commentRequestDto, user, post));
        return new CommentResponseDto(comment);
    }

    // 수정
    @Transactional
    public ResponseEntity<String> update(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(id);
        UserRoleEnum role = user.getRole();

        if (role == UserRoleEnum.ADMIN) {
            comment.update(commentRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 댓글 수정 성공");
        } else if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 당신의 댓글이 아닙니다.");
        } else {
            comment.update(commentRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시물 수정 성공");
        }
    }

    // 삭제
    public ResponseEntity<String> delete(Long id, User user) throws IllegalArgumentException {
        Comment comment = findComment(id);
        UserRoleEnum role = user.getRole();

        if (role == UserRoleEnum.ADMIN) {
            commentRepository.delete(comment);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 댓글 삭제 성공");
        } else if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 당신 댓글이 아닙니다.");
        } else {
            commentRepository.delete(comment);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 댓글 삭제 성공");
        }

    }


    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 댓글이 없습니다."));
    }
}
