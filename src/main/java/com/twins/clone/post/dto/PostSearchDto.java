package com.twins.clone.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchDto {
    private String title;
    private String category;
    private String contents;
}
