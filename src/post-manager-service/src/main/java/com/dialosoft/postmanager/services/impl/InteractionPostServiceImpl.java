package com.dialosoft.postmanager.services.impl;

import com.dialosoft.postmanager.mapper.CommentsMapper;
import com.dialosoft.postmanager.models.dto.Comments;
import com.dialosoft.postmanager.models.entities.CommentsEntity;
import com.dialosoft.postmanager.models.entities.PostEntity;
import com.dialosoft.postmanager.models.web.request.CreateCommentCommonAttributes;
import com.dialosoft.postmanager.models.web.request.CreateReactionCommonAttributes;
import com.dialosoft.postmanager.repository.CommentsRepository;
import com.dialosoft.postmanager.repository.PostManagerRepository;
import com.dialosoft.postmanager.services.InteractionsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InteractionPostServiceImpl implements InteractionsPostService {
    private final CommentsRepository commentsRepository;
    private final PostManagerRepository postManagerRepository;
    private final CommentsMapper commentsMapper;


    @Override
    public void createComment(CreateCommentCommonAttributes request) {

        if (request.getPostId() != null && !request.getPostId().isEmpty()) {
            // validation of the post must be exist
            PostEntity post = postManagerRepository.findById(UUID.fromString(request.getPostId()))
                    .orElseThrow(() -> new RuntimeException("Post not found"));


            if (request.getCommentId() != null && !request.getCommentId().isEmpty()) {
                // validation of the commentId for create a reply
                CommentsEntity parentComment = commentsRepository.findById(UUID.fromString(request.getCommentId()))
                        .orElseThrow(() -> new RuntimeException("Comment not found"));

                CommentsEntity replayComment = commentsMapper.toEntity(request);
                replayComment.setCreationTime(LocalDateTime.now());
                replayComment.setParentCommentId(parentComment.getId());
              //  replayComment.setParentComment(parentComment);
                replayComment.setPost(post);
                commentsRepository.save(replayComment);
                post.getComments().add(replayComment);
                postManagerRepository.save(post);


            } else {
                // other way just create a new comment
                CommentsEntity entity = commentsMapper.toEntity(request);

                entity.setPost(post);
                entity.setCreationTime(LocalDateTime.now());
                commentsRepository.save(entity);

                post.getComments().add(entity);
                postManagerRepository.save(post);
            }
        }

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
    public List<Comments> getAllCommentsFromPost(String id) {
            List<Comments> allComments = commentsMapper.toCommentList(
                    postManagerRepository.findCommentsById(UUID.fromString(id)).get());


            Map<String, Comments> commentsMap = new HashMap<>();
            for (Comments comment : allComments) {
                commentsMap.put(comment.getId(), comment);
            }

            List<Comments> rootComments = new ArrayList<>();


            for (Comments comment : allComments) {
                if (comment.getParentCommentId() != null) {

                    Comments parentComment = commentsMap.get(comment.getParentCommentId());
                    if (parentComment != null) {

                        if (parentComment.getRepliesComment() == null) {
                            parentComment.setRepliesComment(new ArrayList<>());
                        }
                        parentComment.getRepliesComment().add(comment);
                    }
                } else {

                    rootComments.add(comment);
                }
            }

            return rootComments;

    }


    @Override
    public void createReaction(CreateReactionCommonAttributes request) {

        if (request.getPostId() != null && !request.getPostId().isEmpty()) {
            Optional<PostEntity> postEntity = postManagerRepository.findById(UUID.fromString(request.getPostId()));
            int precedentPositivePostCount = postEntity.get().getPositiveReaction() != null ? postEntity.get().getPositiveReaction() : 0;
            int precedentNegativePostCount = postEntity.get().getNegativeReaction() != null ? postEntity.get().getNegativeReaction() : 0;
            if (request.getReaction()) {
                postEntity.get().setPositiveReaction(precedentPositivePostCount + 1);
            }
            if (!request.getReaction()) {
                postEntity.get().setNegativeReaction(precedentNegativePostCount + 1);
            }
            postManagerRepository.save(postEntity.get());
        }
        if (request.getCommentId() != null && !request.getCommentId().isEmpty()) {
            Optional<CommentsEntity> comment = commentsRepository.findById(UUID.fromString(request.getCommentId()));
            int precedentPositiveCommentCount = comment.get().getPositiveReaction() != null ? comment.get().getPositiveReaction() : 0;
            int precedentNegativeCommentCount = comment.get().getNegativeReaction() != null ? comment.get().getNegativeReaction() : 0;
            if (request.getReaction()) {
                comment.get().setPositiveReaction(precedentPositiveCommentCount + 1);
            }
            if (!request.getReaction()) {
                comment.get().setNegativeReaction(precedentNegativeCommentCount + 1);
            }
            commentsRepository.save(comment.get());
        }

    }

    @Override
    public void modifyReaction(CreateReactionCommonAttributes request) {
         if (request.getPostId() != null && !request.getPostId().isEmpty() && request.getReaction() != null){
             int count = request.getReaction()? 1 : -1;
             PostEntity post = postManagerRepository.findById(UUID.fromString(request.getPostId()))
                     .orElseThrow(() -> new RuntimeException("Post not found"));
             int precedentPositivePostCount = post.getPositiveReaction() != null ? post.getPositiveReaction() : 0;
             int precedentNegativePostCount = post.getNegativeReaction() != null ? post.getNegativeReaction() : 0;

             if (request.getCommentId() != null && !request.getCommentId().isEmpty()){
                 CommentsEntity existingComment = commentsRepository.findById(UUID.fromString(request.getCommentId()))
                         .orElseThrow(() -> new RuntimeException("Comment not found"));

             }


         }
    }

    @Override
    public void deleteReaction(String id) {

    }

}