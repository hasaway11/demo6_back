package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

@Validated
@RestController
public class CommentController {
    @Autowired
    private CommentService service;

    @Operation(summary="댓글 작성", description="댓글을 작성하면 글의 모든 댓글을 출력")
    @Secured("ROLE_USER")
    @PostMapping("/api/comments")
    public ResponseEntity<List<Comment>> write(@Valid CommentDto.Craete dto, BindingResult br, Principal principal) {
        return ResponseEntity.ok(service.write(dto, principal.getName()));
    }

    @Operation(summary="댓글 삭제", description="댓글을 작성하면 글의 모든 댓글을 출력")
    @Secured("ROLE_USER")
    @DeleteMapping("/api/comments")
    public ResponseEntity<List<Comment>> delete(@Valid CommentDto.Delete dto, BindingResult br, Principal principal) {
        return ResponseEntity.ok(service.delete(dto, principal.getName()));
    }
}