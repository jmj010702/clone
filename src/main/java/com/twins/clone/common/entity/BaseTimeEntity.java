package com.twins.clone.common.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseTimeEntity {
    @CreationTimestamp
    private LocalDateTime creatTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
