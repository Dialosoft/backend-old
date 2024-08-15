package com.biznetbb.postmanager.services.impl;

import com.biznetbb.postmanager.mapper.CommentsMapper;
import com.biznetbb.postmanager.models.entities.CommentsEntity;
import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.CreateCommentCommonAttributes;
import com.biznetbb.postmanager.models.web.request.CreateReactionCommonAttributes;
import com.biznetbb.postmanager.repository.CommentsRepository;
import com.biznetbb.postmanager.repository.PostManagerRepository;
import com.biznetbb.postmanager.services.InteractionsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InteractionPostServiceImpl implements InteractionsPostService {
    private final CommentsRepository commentsRepository;
    private final PostManagerRepository postManagerRepository;
    private final CommentsMapper commentsMapper;


    @Override
    public void createComment(CreateCommentCommonAttributes request) {

        PostEntity post = postManagerRepository.findById(UUID.fromString(request.getPostId()))
                .orElseThrow(() -> new RuntimeException("Post not found"));

        CommentsEntity entity = commentsMapper.toEntity(request);

        entity.setPost(post);
        entity.setCreationTime(LocalDateTime.now());
        commentsRepository.save(entity);

        post.getComments().add(entity);
        postManagerRepository.save(post);

    }

    @Override
    public void modifyComment(CreateCommentCommonAttributes request) {
        if (request.getCommentId() != null) {

            PostEntity post = postManagerRepository.findById(UUID.fromString(request.getPostId()))
                    .orElseThrow(() -> new RuntimeException("Post not found"));


            CommentsEntity existingComment = commentsRepository.findById(UUID.fromString(request.getCommentId()))
                    .orElseThrow(() -> new RuntimeException("Comment not found"));


            existingComment.setContent(request.getContent());
            existingComment.setUsername(request.getUsername());


            existingComment.setPost(post);

            commentsRepository.save(existingComment);
        }
    }


    @Override
    public void deleteComment(String id) {

        CommentsEntity existingComment = commentsRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Comment not found"));

               commentsRepository.deleteById(UUID.fromString(id));

    }


    @Override
    public void createReaction(CreateReactionCommonAttributes request) {

        if(request.getPostId()!= null && !request.getPostId().isEmpty()){
            Optional<PostEntity> postEntity =  postManagerRepository.findById(UUID.fromString(request.getPostId()));
            int precedentPositivePostCount = postEntity.get().getPositiveReaction() != null ? postEntity.get().getPositiveReaction() : 0;
            int precedentNegativePostCount = postEntity.get().getNegativeReaction()!= null ? postEntity.get().getNegativeReaction() : 0;
          if (request.getReaction()){
              postEntity.get().setPositiveReaction(precedentPositivePostCount + 1);
          }
          if(!request.getReaction()){
              postEntity.get().setNegativeReaction(precedentNegativePostCount + 1);
          }
            postManagerRepository.save(postEntity.get());
        }
        if(request.getCommentId() != null && !request.getCommentId().isEmpty()){
            Optional<CommentsEntity> comment =  commentsRepository.findById(UUID.fromString(request.getCommentId()));
            int precedentPositiveCommentCount = comment.get().getPositiveReaction() != null ? comment.get().getPositiveReaction() : 0;
            int precedentNegativeCommentCount = comment.get().getNegativeReaction()!= null ? comment.get().getNegativeReaction() : 0;
            if (request.getReaction()){
                comment.get().setPositiveReaction(precedentPositiveCommentCount + 1);
            }
            if(!request.getReaction()){
                comment.get().setNegativeReaction(precedentNegativeCommentCount + 1);
            }
            commentsRepository.save(comment.get());
        }

    }

    @Override
    public void modifyReaction(CreateReactionCommonAttributes request) {

    }

    @Override
    public void deleteReaction(String id) {

    }

}
