package com.biznetbb.postmanager.models.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
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
     @OneToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "reaction_id", referencedColumnName = "id")
     ReactionsEntity reactions;
     @Column
     LocalDateTime creationTime;
}
