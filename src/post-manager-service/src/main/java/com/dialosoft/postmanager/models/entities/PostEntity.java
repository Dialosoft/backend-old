package com.dialosoft.postmanager.models.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="post-table")
@Entity
public class PostEntity {
     @Id
     @Column(nullable = false, updatable = false)
     @GeneratedValue(strategy = GenerationType.UUID)
     UUID id;
     @Column()
     String username;
     @Column()
     String content;
     @Column()
     byte[] multimedia;
     @Column
     Integer positiveReaction;
     @Column
     Integer negativeReaction;
     @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
     List<CommentsEntity> comments;
}
