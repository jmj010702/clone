package com.twins.clone.author.entity;

import com.twins.clone.common.entity.BaseTimeEntity;
import com.twins.clone.common.entity.DelYN;
import com.twins.clone.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Author extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;
    @NotBlank
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DelYN delYN = DelYN.N;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Post> postList = new ArrayList<>();


    public void delYN(DelYN delYN) {
        this.delYN = delYN;
    }

    public void updatePw(String password) {
        this.password = password;
    }
}
