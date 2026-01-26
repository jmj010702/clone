package com.twins.clone.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long authorid;
    private final String email;
    private final String password;
    private final String role;

    //    필드 실제 데이터
    public CustomUserDetails(Long authorid, String email, String password, String role) {
        this.authorid = authorid;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    //    권한 목록 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //role == user라면  [simpleGrantedAuthoriy("USER")] 반환
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    //    사용자 식별자 반환(사용자의 ID값{현재 id를 email로 쓰고있음})
    @Override
    public String getUsername() {
//        email반환
        return email;
    }

    //    아래 모든 반환값이 true면 정상 하나라도 있으면 로그인 거부
//    계정 만료 현황 EX) 무료 체험판 같은거 체크할때 씀
    @Override
    public boolean isAccountNonExpired() {
        return true; //만약 만료일이 있다면 return author.getExpiredDate.isAfter(LocalDate.now));
    }

    //    계정 잠금 안됨 EX) 비밀번호 5회 틀렸을 때 ->  계정 자동 잠금
    @Override
    public boolean isAccountNonLocked() {
        return true; //잠금 상태가 있다면 return !author.isLocked();
    }

    //    비밀번호 만료 안됨 EX) 비밀번호 변경 주기
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //90일마다 바꿔야 할 때 return author.getPasswordChangeDate().plusDays(90).isAfter(LocalDate.now));
    }

    //    계정 활성화됨 EX) 회원 가입 후 이메일 인증 전
    @Override
    public boolean isEnabled() {
        return true; //Author에 status 필드가 있다면 return author.getstatus().equals("Active);
    }
}
