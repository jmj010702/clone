package com.twins.clone.post.service;

import com.twins.clone.author.entity.Author;
import com.twins.clone.author.entity.Role;
import com.twins.clone.author.repository.AuthorRepository;
import com.twins.clone.common.entity.DelYN;
import com.twins.clone.post.dto.PostCreateDto;
import com.twins.clone.post.dto.PostDetailDto;
import com.twins.clone.post.dto.PostListDto;
import com.twins.clone.post.entity.Post;
import com.twins.clone.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }


    public void save(PostCreateDto pc_dto, String email) {
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 회원"));
        postRepository.save(pc_dto.toEntity(author));
    }

    public void delete(Long id) {
        Post post = postRepository.findByIdAndDelYN(id, DelYN.N).orElseThrow(() -> new EntityNotFoundException("없는 글"));
        Author author = authorRepository.findById(post.getAuthor().getId()).orElseThrow(() -> new EntityNotFoundException("없는 유저"));
        if (!post.getAuthor().getId().equals(author.getId()) && !author.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("권한이 없습니다");
        }
        post.updateDel(DelYN.Y);
    }

    public List<PostListDto> findAll() {
        return postRepository.findAllByDelYN(DelYN.N).stream().map(PostListDto::fromEntity).toList();
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findByIdAndDelYN(id, DelYN.N).orElseThrow(() -> new EntityNotFoundException("없는 글"));
        return PostDetailDto.fromEntity(post);
    }
}
