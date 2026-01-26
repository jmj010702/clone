package com.twins.clone.post.repository;

import com.twins.clone.common.entity.DelYN;
import com.twins.clone.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    int countPostByAuthorId(Long id);

    Optional<Post> findByIdAndDelYN(Long id, DelYN delYN);

    List<Post> findAllByDelYN(DelYN delYN);
}
