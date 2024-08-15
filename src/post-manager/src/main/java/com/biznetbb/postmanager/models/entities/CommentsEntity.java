package com.biznetbb.postmanager.models.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="comments-table")
@Entity
public class CommentsEntity {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String content;
    @Column
    Integer positiveReaction;
    @Column
    Integer negativeReaction;
    @ManyToOne
    @JoinColumn(name = "post_id")
    PostEntity post;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reaction_id", referencedColumnName = "id")
    ReactionsEntity reactions;
    @Column
    LocalDateTime creationTime;
}
