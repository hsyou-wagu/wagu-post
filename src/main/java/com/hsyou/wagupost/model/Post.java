package com.hsyou.wagupost.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated;
    @Column(nullable = false)
    private boolean removed = false;
    private String hashtag;
    private long accountId;

    public PostDTO toDTO(){
        return PostDTO.builder()
                .id(this.getId())
                .contents(this.getContents())
                .created(this.getCreated())
                .updated(this.getUpdated())
                .removed(this.isRemoved())
                .hashtag(this.getHashtag())
                .accountId(this.getAccountId())
                .build();
    }
}
