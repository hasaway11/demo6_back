package com.example.demo;

import com.example.demo.dao.PostDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Demo6ApplicationTests {
  @Autowired
  private PostDao dao;

  @Test
  void contextLoads() {
  }

}
