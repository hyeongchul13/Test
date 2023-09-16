package com.sparta.first_project.service;

import com.sparta.first_project.dto.CommentResponseDto;
import com.sparta.first_project.dto.PostRequestDto;
import com.sparta.first_project.dto.PostResponseDto;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    //생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        return new PostResponseDto(post);
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostList() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc(); //List<Post> 내림차순으로 가져오기
        List<PostResponseDto> responseDto = new ArrayList<>();  // List<PostResponseDto> 빈통 만들기

        for (Post post : postList) {
            List<Comment> commentList = commentRepository.findAllByPost(post); // List<Comment>  가져오기
            PostResponseDto postResponseDto = new PostResponseDto(post);    // PostResponseDto 빈통 만들기

            List<CommentResponseDto> commentResponseDtos = new ArrayList<>();   // List<CommentResponseDto> 빈통 만들기
            for (Comment comment : commentList) {
                commentResponseDtos.add(new CommentResponseDto(comment));   // List<CommentResponseDto>에 CommnetResponseDto를 add하기
            }

            postResponseDto.setCommentList(commentResponseDtos);    // postResponseDto 에 CommentList 세팅하기
            responseDto.add(postResponseDto);   // List<PostResponseDto> 에 postResponseDto를 add 하기
        }
        return responseDto;
    }

    // 게시물 id로 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("게시물이 존재하지 않습니다"));

        PostResponseDto postResponseDto = new PostResponseDto(post);    // PostResponseDto 빈통 만들기
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByModifiedAt(post); // List<Comment> 가져오기
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtos.add(new CommentResponseDto(comment));
        }
        postResponseDto.setCommentList(commentResponseDtos);
        return postResponseDto;
    }

    // 수정
    @Transactional
    public ResponseEntity<String> update(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);
        UserRoleEnum role = user.getRole();

        if (role == UserRoleEnum.ADMIN) {
            post.update(requestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 게시물 수정 성공");
        } else if (!post.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 당신의 게시물이 아닙니다.");
        } else {
            post.update(requestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시물 수정 성공");
        }
    }

    // 삭제
    public ResponseEntity<String> delete(Long id, User user) throws IllegalArgumentException {
        Post post = findPost(id);
        UserRoleEnum role = user.getRole();

        if (role == UserRoleEnum.ADMIN) {
            postRepository.delete(post);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 관리자 권한으로 게시물 삭제 성공");
        } else if (!post.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 당신의 게시물이 아닙니다.");
        } else {
            postRepository.delete(post);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시물 삭제 성공");
        }

    }

    // DB에서 찾기
    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }
}