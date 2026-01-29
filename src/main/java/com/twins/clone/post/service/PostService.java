package com.twins.clone.post.service;

import com.twins.clone.author.entity.Author;
import com.twins.clone.author.entity.Role;
import com.twins.clone.author.repository.AuthorRepository;
import com.twins.clone.common.entity.DelYN;
import com.twins.clone.post.dto.PostCreateDto;
import com.twins.clone.post.dto.PostDetailDto;
import com.twins.clone.post.dto.PostListDto;
import com.twins.clone.post.dto.PostSearchDto;
import com.twins.clone.post.entity.Post;
import com.twins.clone.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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

//    public Page<PostListDto> pageFindAll(Pageable pageable) {
//        Page<Post> post = postRepository.findAllByDelYN(pageable, DelYN.N);
//        return post.map(PostListDto::fromEntity);
//    }

    public Page<PostListDto> pageFindAll(Pageable pageable, PostSearchDto ps_dto) {
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                predicateList.add(criteriaBuilder.equal(root.get("DelYN"), DelYN.N));

                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N"));
                if (ps_dto.getTitle() != null) {
                    predicateList.add(criteriaBuilder
                            .like(root.get("title"), "%" + ps_dto.getTitle() + "%"));
                }
                if (ps_dto.getCategory() != null) {
                    predicateList.add(criteriaBuilder
                            .equal(root.get("category"), "%" + ps_dto.getCategory() + "%"));
                }
                if (ps_dto.getContents() != null) {
                    predicateList.add(criteriaBuilder
                            .like(root.get("contents"), "%" + ps_dto.getContents() + "%"));
                }

                Predicate[] predicatesArr = new Predicate[predicateList.size()
                        ];

                for (int i = 0; i < predicatesArr.length; i++) {
                    predicatesArr[i] = predicateList.get(i);
                }

                Predicate predicate
                        = criteriaBuilder.and(predicatesArr);


                return predicate;
            }
        };

        Page<Post> postList = postRepository.findAllByDelYN(specification, pageable, DelYN.N);
        return postList.map(PostListDto::fromEntity);
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findByIdAndDelYN(id, DelYN.N).orElseThrow(() -> new EntityNotFoundException("없는 글"));
        return PostDetailDto.fromEntity(post);
    }
}
