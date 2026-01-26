package com.twins.clone.author.service;

import com.twins.clone.author.dto.*;
import com.twins.clone.author.entity.Author;
import com.twins.clone.author.repository.AuthorRepository;
import com.twins.clone.common.entity.DelYN;
import com.twins.clone.post.entity.Post;
import com.twins.clone.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(AuthorCreateDto ac_dto) {
        if (authorRepository.findByEmail(ac_dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email");
        }
        Author author = ac_dto.toEntity(passwordEncoder.encode(ac_dto.password));
        author.getPostList().add(Post.builder().title("안녕하세요").contents("안녕하세요").category("인사").author(author).build());
        authorRepository.save(author);
    }

    public Author login(AuthorLoginDto al_dto) {
//        Optional<Author> author = authorRepository.findByEmail(al_dto.getEmail());
//        boolean check = true;
//        if (author.isEmpty()) {
//            check = false;
//        } else {
//            if (!passwordEncoder.matches(al_dto.getPassword(), author.get().getPassword())) {
//                check = false;
//            }
//        }
//
//        if (check) {
//            return author.get();
//        } else {
//            throw new IllegalArgumentException("없는 entity");
//        }
        Author author = authorRepository.findByEmail(al_dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("이메일"));
        if (passwordEncoder.matches(al_dto.getPassword(), author.getPassword())) {
            return author;
        } else {
            throw new IllegalArgumentException("ad");
        }
    }

    public void delete(Long pathid, Long authorid, String role) {
        Author author = authorRepository.findByIdAndDelYN(pathid, DelYN.N).orElseThrow(() -> new EntityNotFoundException("없는 entity"));

        if (!pathid.equals(authorid) && !role.equals("ADMIN")){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        author.delYN(DelYN.Y);

    }

    public void updatepw(AuthorUpdateDto au_dto) {
        Author author = authorRepository.findByEmail(au_dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("없는 이메일입니다"));
        author.updatePw(au_dto.getPassword());
    }

    public List<AuthorListDto> findAll() {
        return authorRepository.findAllByDelYN(DelYN.N).stream().map(AuthorListDto::fromEntity).collect(Collectors.toList());
    }

    public AuthorDetailDto findById(Long id) {
        Author author = authorRepository.findByIdAndDelYN(id, DelYN.N).orElseThrow(() -> new EntityNotFoundException("없는 Entity"));

        int count = postRepository.countPostByAuthorId(id);

        return AuthorDetailDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .count(count)
                .password(author.getPassword())
                .build();
    }


}
