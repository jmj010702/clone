package com.twins.clone.common.init;


import com.twins.clone.author.entity.Author;
import com.twins.clone.author.entity.Role;
import com.twins.clone.author.repository.AuthorRepository;
import com.twins.clone.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Initialdata implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Initialdata(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        if (authorRepository.findByEmail("ADMIN@naver.com").isPresent()) {
            return;
        }
        authorRepository.save(Author.builder().name("ADMIN").email("ADMIN@naver.com").password(passwordEncoder.encode("ADMIN1234")).role(Role.ADMIN).build());
    }
}
