package com.twins.clone.author.dto;

import com.twins.clone.author.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorListDto {
    private Long id;
    private String name;
    private String email;

    public static AuthorListDto fromEntity(Author author) {
        return AuthorListDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }
}
