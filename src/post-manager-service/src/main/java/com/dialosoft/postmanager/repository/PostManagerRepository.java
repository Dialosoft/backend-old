package com.dialosoft.postmanager.repository;

import com.dialosoft.postmanager.models.entities.CommentsEntity;
import com.dialosoft.postmanager.models.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface PostManagerRepository extends JpaRepository<PostEntity, UUID> {
    // remember using optinal for the methods of this repository

    Optional<List<PostEntity>> findByUsername(String username);
    Optional<List<PostEntity>> findByForumId(String forumId);
    @Query("SELECT c FROM CommentsEntity c WHERE c.post.id = :postId")
    Optional<List<CommentsEntity>> findCommentsById(@Param("postId")  UUID id);
}
