package com.hsyou.wagupost.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private long id;
    private String contents;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean removed = false;

    public Post toEntity(){
        return Post.builder()
                .id(id)
                .contents(contents)
                .created(created)
                .updated(updated)
                .removed(removed)
                .build();
    }

}
