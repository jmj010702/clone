package com.twins.clone.common.security;

import com.twins.clone.author.entity.Author;
import com.twins.clone.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //UserDetailsService : spring이 제공하는 인터페이스 security가 사용자 정보 어떻게 가져와 할때 쓰는 약속된 인터페이스
public class CustomUserDetailService implements UserDetailsService {
    //    DB에서 Author찾기 위해 Repository 주입받음
    private final AuthorRepository authorRepository;

    @Autowired
    public CustomUserDetailService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override //로그인할 때 Spring security가 자동으로 호출 (파라미터 이름은 username이지만 email을 넣으면 됨)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(
                author.getId(),
                author.getEmail(),
                author.getPassword(),
                author.getRole().name()// name()메서드 : ENUM을 String으로 변환
        );
    }
}
