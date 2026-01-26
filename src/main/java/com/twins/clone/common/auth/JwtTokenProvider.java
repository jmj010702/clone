package com.twins.clone.common.auth;

import com.twins.clone.author.entity.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret_key}")
    private String st_secret_key;
    private Key secret_key;

    @PostConstruct
    public void init() {
        secret_key = new SecretKeySpec(Base64.getDecoder().decode(st_secret_key), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(Author author) {
        Claims claims = Jwts.claims().setSubject(author.getEmail());
        claims.put("role", author.getRole().toString());
        claims.put("authorId", author.getId());
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 30 * 60 * 1000L))
                .signWith(secret_key)
                .compact();
        return token;
    }

    //    토큰 검증 코드
    public boolean validateToken(String token) {
        try {
//            jwt를 파싱하기 위한 빌더
            Jwts.parserBuilder()
//                    토큰 만들 때 사용한 secret_key로 서명 검증 /우리 서버의 secret_key
                    .setSigningKey(secret_key)
                    .build()
//                   사용자가 보낸 token/ 토큰 파싱+ 검증
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
//        build까지 토큰을 파싱할 준비 및 서명 검증도 함께 수행
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
//                토큰을 파싱하고 검증 , 성공하면 jws<claims> 객체 반환
                .parseClaimsJws(token)
//                토큰의 페이로드 부분 가져옴 , claims = 토큰 안에 담긴 정보들
                .getBody();
//        claims에서 subject 값을 꺼냄
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public Long getAuthorIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("authorId", Long.class);
    }
}
