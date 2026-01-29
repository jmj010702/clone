package com.twins.clone.post.dto;

import com.twins.clone.author.entity.Author;
import com.twins.clone.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    private String title;
    private String contents;
    private String category;

    @Builder.Default
    private String appointment = "N";
    @Builder.Default
    private LocalDateTime appoimentTime = LocalDateTime.now();


    public Post toEntity(Author author) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .category(this.category)
                .author(author)
                .build();
    }

}
