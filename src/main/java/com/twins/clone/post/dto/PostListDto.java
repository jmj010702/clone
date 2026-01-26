package com.twins.clone.post.dto;

import com.twins.clone.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private Long id;
    private String title;
    private String category;
    private String contents;

    public static PostListDto fromEntity(Post post) {
        return PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .contents(post.getContents())
                .build();
    }

}
