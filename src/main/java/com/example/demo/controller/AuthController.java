package com.example.demo.controller;

import java.security.*;
import java.util.*;

import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import lombok.*;
import org.springframework.web.servlet.view.*;

@RequiredArgsConstructor
@Controller
public class AuthController {
	// 프론트엔드에서 현재 로그인 상태를 물어오면 응답 
	// 로그인한 경우 : 200 + 로그인 아이디
	// 비로그인 : 409 + null
	@GetMapping(path="/api/auth/check")
	public ResponseEntity<Map<String, String>> checkLogin(Principal principal, HttpSession session) {
		if(principal!=null)
			return ResponseEntity.ok(Map.of("username", principal.getName()));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}
}
