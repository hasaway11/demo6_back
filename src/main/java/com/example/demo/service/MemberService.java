package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.util.*;
import jakarta.validation.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.security.*;
import java.util.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;

  public boolean checkUsername(MemberDto.UsernameCheck dto) {
    return !memberDao.existsByUsername(dto.getUsername());
  }

  public Member signup(MemberDto.Create dto) {
    // 1. 비밀번호 암호화
    String encodedPassword = encoder.encode(dto.getPassword());
    // 2. 프사를 업로드했으면 인코딩, 업로드하지 않았으면 기본 프사를 저장
    MultipartFile profile = dto.getProfile();
    // 프론트에 <input type='file' name='profile'>이 없다면 profile이 null이 된다
    // 이 경우 profile.isEmpty()는 null pointer exception(NPE)
    boolean 프사_존재 = profile!=null && !profile.isEmpty();
    String base64Image = "";
    try {
      if(프사_존재) {
        base64Image = Demo6Util.convertToBase64(profile);
      } else {
        base64Image = Demo6Util.getDefaultBase64Profile();
      }
    } catch(IOException e) {
      base64Image = Demo6Util.getDefaultBase64Profile();
    }
    // 3. 암호화된 비밀번호, base64이미지를 가지고 dto를 member로 변환
    Member member = dto.toEntity(encodedPassword, base64Image);
    memberDao.save(member);
    return member;
  }

  public Optional<String> searchUseraname(String email) {
    return memberDao.findUsernameByEmail(email);
  }

  public Optional<String> getTemporaryPassword(MemberDto.GeneratePassword dto) {
    // 1. 아이디와 이메일이 일치하는 사용자가 있는 지 확인
    // 2. 사용자가 없을 경우 비어있는 Optional을 리턴 -> 컨트롤러에서 if문으로 처리
    // 3. 있을 경우 임시비밀번호를 생성
    // 4. 임시비밀번호를 암호화해서 업데이트
    // 5. 비밀번호를 Optional로 리턴
    boolean isExist = memberDao.existsByUsernameAndEmail(dto);
    if(!isExist)
      return Optional.empty();
    String newPassword = RandomStringUtils.secure().nextAlphanumeric(20);
    memberDao.updatePassword(dto.getUsername(), newPassword);
    return Optional.ofNullable(newPassword);
  }

  public MemberDto.Read read(String loginId) {
    Member member = memberDao.findByUsername(loginId);
    return member.toRead();
  }

  public MemberDto.Read changeProfile(MultipartFile profile, String loginId) {
    String base64Image = "";
    try {
      base64Image = Demo6Util.convertToBase64(profile);
      memberDao.updateProfile(base64Image, loginId);
    } catch(IOException e) {
      System.out.println(e.getMessage());
    }
    return memberDao.findByUsername(loginId).toRead();
  }

  public boolean changePassword(MemberDto.PasswordChange dto, String loginId) {
    // 기존 암호화된 비밀번호를 읽어와 비밀번호가 맞는 지 확인 -> 틀리면 false
    String encodedPassword = memberDao.findPasswordByUsername(loginId);
    if(!encoder.matches(dto.getCurrentPassword(), encodedPassword))
      return false;
    // 비밀번호가 일치한 경우 새 비밀번호로 업데이트
    return memberDao.updatePassword(loginId, encoder.encode(dto.getNewPassword()))==1;
  }

  public void resign(String loginId) {
    memberDao.delete(loginId);
  }

  public boolean checkPassword(String password, String loginId) {
    String encodedPassword = memberDao.findPasswordByUsername(loginId);
    return (encoder.matches(password, encodedPassword));
  }
}







