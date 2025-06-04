package com.example.demo;

import com.example.demo.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
public class MemberServiceTest {
  @Autowired
  private MemberService service;

  @Test
  public void testMail() {
    service.sendMail("hasaway@gmail.com", "hasaway1@daum.net", "제목입니다", "내용입니다");
  }
}
