package com.biznetbb.postmanager.services.impl;

import com.biznetbb.postmanager.mapper.CommentsMapper;
import com.biznetbb.postmanager.mapper.PostManagerMapper;
import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.PostManagerCommonAttributes;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;
import com.biznetbb.postmanager.repository.PostManagerRepository;
import com.biznetbb.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostManagerServiceImpl implements PostManagerService {

    //TODO terminar toda la gestion
    private final PostManagerMapper mapper;
    private  final PostManagerRepository postManagerRepository;
    private final CommentsMapper commentsMapper;
    @Override
    public void CreateNewPost(PostManagerCommonAttributes request) {

        if (request != null){
            PostEntity entity = mapper.toEntity(request);
            entity.setCreationTime(LocalDateTime.now());
            postManagerRepository.save(entity);
        }
    }

    @Override
    public void DeletePost(String id) {
        if(postManagerRepository.existsById(UUID.fromString(id))){
            postManagerRepository.deleteById(UUID.fromString(id));
        }
    }

    @Override
    public void ModifiedPost(PostManagerCommonAttributes request) {
        Optional<PostEntity> entity = postManagerRepository.findById(UUID.fromString(request.getId()));
        if (entity.isPresent()){
            entity = Optional.ofNullable(mapper.toEntity(request));
            postManagerRepository.save(entity.get());
        }
    }

    @Override
    public PostManagerResponse GetPost(String id) {
        Optional<PostEntity> entity = postManagerRepository.findById(UUID.fromString(id));
        if (entity.isPresent()){
            PostManagerResponse response;
            response = mapper.toResponse(entity.get());
            response.setComments(commentsMapper.toCommentList(entity.get().getComments()));
           return response;
        }
      return null;
    }

    @Override
    public List<PostManagerResponse> GetMultiPost(String username) {
        Optional<List<PostEntity>> entities = postManagerRepository.findByUsername(username);
        return entities.map(mapper::toResponseList).orElse(null);
    }
}
