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
public class AuthorCreateDto {

    public String name;
    public String email;
    public String password;

    public Author toEntity(String encodepassword) {
        return Author.builder()
                .name(this.name
                ).email(this.email)
                .password(encodepassword).build();
    }
}
