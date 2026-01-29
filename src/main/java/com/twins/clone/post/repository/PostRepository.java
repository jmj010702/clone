package com.twins.clone.post.repository;

import com.twins.clone.common.entity.DelYN;
import com.twins.clone.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    int countPostByAuthorId(Long id);

    Optional<Post> findByIdAndDelYN(Long id, DelYN delYN);

    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllByDelYN(DelYN delYN);

    List<Post> findAllFetchInnerJoin();

//  검색용 JPA
    Page<Post> findAllByDelYN(Specification<Post> specification, Pageable pageable, DelYN delYN);

    List<Post> findAllByAppointment(String appointment);
}
