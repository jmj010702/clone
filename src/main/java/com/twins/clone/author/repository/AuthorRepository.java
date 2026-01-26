package com.twins.clone.author.repository;

import com.twins.clone.author.entity.Author;
import com.twins.clone.common.entity.DelYN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByEmail(String email);

    Optional<Author> findByIdAndDelYN(Long id, DelYN delYN);

    List<Author> findAllByDelYN(DelYN delYN);
}
