package com.example.demo.security;

import com.example.demo.dao.*;
import com.example.demo.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class Demo6AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  @Autowired
  private MemberDao memberDao;
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    // 로그인 실패 횟수 리셋
    String loginId = authentication.getName();
    memberDao.reset로그인실패횟수(loginId);

    // JSON 응답 생성
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("username", loginId);
    ResponseUtil.sendJsonResponse(response, 200, responseBody);
  }
}








