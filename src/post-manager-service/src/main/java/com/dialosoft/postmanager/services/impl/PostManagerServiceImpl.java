package com.dialosoft.postmanager.services.impl;

import com.dialosoft.postmanager.mapper.CommentsMapper;
import com.dialosoft.postmanager.mapper.PostManagerMapper;
import com.dialosoft.postmanager.models.entities.PostEntity;
import com.dialosoft.postmanager.models.web.request.PostManagerCommonAttributes;
import com.dialosoft.postmanager.models.web.response.PostManagerResponse;
import com.dialosoft.postmanager.repository.PostManagerRepository;
import com.dialosoft.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostManagerServiceImpl implements PostManagerService {

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
    public void savePostAsFavorite(String postId, Boolean isFavorite) {
        Optional<PostEntity> entity = postManagerRepository.findById(UUID.fromString(postId));
        if (isFavorite){
            entity.get().setIsFavorite(true);
            entity.get().setSaveTime(LocalDateTime.now());
        }
        else entity.get().setIsFavorite(false);
        postManagerRepository.save(entity.get());
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

    @Override
    public List<PostManagerResponse> GetMultiPostFromForum(String forumId) {
        Optional<List<PostEntity>> entities = postManagerRepository.findByForumId(forumId);
        return entities.map(mapper::toResponseList).orElse(null);
    }

    @Override
    public List<PostManagerResponse> getFavoritesPostFromUser(String username) {
        Optional<List<PostEntity>> entities = postManagerRepository.findFavoritePost(username);
        return entities.map(mapper::toResponseList).orElse(null);
    }
}