package com.twins.clone.post.entity;

import com.twins.clone.author.entity.Author;
import com.twins.clone.common.entity.DelYN;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    private String category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Author author;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DelYN delYN = DelYN.N;

    public void updateDel(DelYN delYN){
        this.delYN = delYN;
    }


}
