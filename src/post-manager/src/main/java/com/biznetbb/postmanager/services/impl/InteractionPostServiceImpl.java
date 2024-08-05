package com.biznetbb.postmanager.services.impl;

import com.biznetbb.postmanager.mapper.CommentsMapper;
import com.biznetbb.postmanager.models.entities.CommentsEntity;
import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.CreateCommentRequest;
import com.biznetbb.postmanager.repository.CommentsRepository;
import com.biznetbb.postmanager.repository.PostManagerRepository;
import com.biznetbb.postmanager.services.InteractionsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InteractionPostServiceImpl implements InteractionsPostService {
    private final CommentsRepository commentsRepository;
    private final PostManagerRepository postManagerRepository;
    private final CommentsMapper commentsMapper;
    // todo terminar gestion de comerntarios
    @Override
    public void createComment(CreateCommentRequest request) {

        PostEntity post = postManagerRepository.findById(UUID.fromString(request.getPostId()))
                .orElseThrow(() -> new RuntimeException("Post not found"));

        CommentsEntity entity = commentsMapper.toEntity(request);

        entity.setPost(post);
        commentsRepository.save(entity);

        post.getComments().add(entity);
        postManagerRepository.save(post);
    }
}
