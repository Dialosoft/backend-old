package com.biznetbb.postmanager.models.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
     @Column(nullable = false,unique = true)
     String username;
     @Column(nullable = false, length = 30, unique = true)
     String content;
     @Column()
     byte[] multimedia;

   //  CommentsEntity Comments;
}
