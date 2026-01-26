package com.twins.clone.common.auth;

import com.twins.clone.common.security.CustomUserDetails;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    //    토큰 존재확인 -> 토큰 검증(서명,만료,형식)-> 검증 통과시 인증 정보 등록 or 검증 실패 시 아무것도 안하고 spring이 차단
    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //    ServletRequest : 클라이언트의 요청 정보
//    ServletResponse : 서버의 응답 정보
//    Filterchain : 다음 필터로 넘기는 도구
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        ServletRequest는 기본 인터페이스라 헤더 접근 불가능
//        HttpServletRequset는 Http전용(헤더 접근 가능)
//        Authoriazation헤더를 읽어야 하니 형변환 필수 / 토큰이 담겨 있음
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

//        Authorization 헤더의 값(토큰) 추출
        String header = httpServletRequest.getHeader("Authorization");
        String token = null;

//        헤더가 존재하고 Bearer로 시작하는지 확인
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

//        토큰 검증
//        jwtTokenProvider.validateToken(token): 토큰이 유효한지 검증하는 코드
        if (token != null && jwtTokenProvider.validateToken(token)) {

//            토큰에 담긴 각 정보를 추출(이걸 해야 사용자의 정보를 알 수 있음)
            String email = jwtTokenProvider.getEmailFromToken(token);
            String role = jwtTokenProvider.getRoleFromToken(token);
            Long authorId = jwtTokenProvider.getAuthorIdFromToken(token);
//            CustomUserDetails 생성
            CustomUserDetails userDetails = new CustomUserDetails(authorId, email, null, role);
            /*
        Authentication 객체 생성
        SimpleGrantedAuthority : Spring Security에서 권한을 표현하는 클래스
        new SimpleGrantedAuthority(role) Role_User같은 권한을 객체로 만듬
        Listof() : 리스트로 감싸기 (한명의 사용자가 여러 권한을 가질 수 있어서)
        결과로 [SimpleGrantedAuthority(ROLE_USER)] 이런게 나옴
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
             */

//        Authentication : SpringSecurity의 인증 정보 인터페이스
//        UsernamePasswordAuthenticationToken : Authentication의 구현체/ 만들어진 객체의 뜻은 인증된 사용자라는 뜻
//        email : 주체 null : 자격증명(비밀번호- jwt에선 불필요) authorities(권한목록)
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//SecurityContextHolder: 현재 인증된 사용자 정보를 보관하는저장소 -> 어디서든 접근 가능
//        .getContext: 요청의 보안 컨텍스트를 가져옴-> 이 안에 인증 정보가 담김
//        .setAuthentication : 아까 만든 auhtentication 객체를 이 안에 넣음
//         아래 코드로 인해 인증된 사용자로 인식
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

//        위에 검증이 끝났으니 다음 필터에게 넘김
        chain.doFilter(request, response);
    }
}
