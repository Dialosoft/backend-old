package com.dialosoft.postmanager.models.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="reactions-table")
@Entity
public class ReactionsEntity {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column()
    String username;
    @Column()
    int countReactions;
    @Column()
    int positiveReaction;
    @Column()
    int negativeReaction;
    @OneToOne(mappedBy = "reactions")
    PostEntity post;
    @OneToOne(mappedBy = "reactions")
    CommentsEntity comment;
}
