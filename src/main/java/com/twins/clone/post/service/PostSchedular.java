package com.twins.clone.post.service;

import com.twins.clone.post.entity.Post;
import com.twins.clone.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@Transactional
public class PostSchedular {

    private final PostRepository postRepository;

    @Autowired
    public PostSchedular(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Scheduled
    public void postupdate() {
        List<Post> postList = postRepository.findAllByAppointment("Y");
        LocalDateTime now2 = LocalDateTime.now();

        for (Post p : postList) {
            if (p.getAppoimentTime().isBefore(
                    now2)) {
                p.updateAppoiment("N");
            }
        }
    }

}
