package com.biznetbb.postmanager.services.impl;

import com.biznetbb.postmanager.mapper.PostManagerMapper;
import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;
import com.biznetbb.postmanager.repository.PostManagerRepository;
import com.biznetbb.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostManagerServiceImpl implements PostManagerService {

    private final PostManagerMapper mapper;
    private  final PostManagerRepository postManagerRepository;
    @Override
    public void CreateNewPost(PostManagerRequest request) {

        if (request != null){
            PostEntity entity = mapper.toEntity(request);
            entity.setMultimedia(Base64.getDecoder().decode(request.getImage()));
            postManagerRepository.save(entity);
        }
    }

    @Override
    public void DeletePost() {

    }

    @Override
    public void ModifiedPost(PostManagerRequest request) {
        Optional<PostEntity> entity = postManagerRepository.findById(request.getId());
        if (entity.isPresent()){
            //entity.setComments(request.getComments());
            entity.get().setContent(request.getContent());
          //  entity.get().setMultimedia(request.getImage());
            postManagerRepository.save(entity.get());
        }
    }

    @Override
    public PostManagerResponse GetPost(String id) {
        Optional<PostEntity> entity = postManagerRepository.findById(UUID.fromString(id));
        if (entity.isPresent()){
            PostManagerResponse response = new PostManagerResponse();
            response = mapper.toResponse(entity.get());
            response.setImage(Base64.getEncoder().encodeToString(entity.get().getMultimedia()));
 //          response.setImage(Arrays.toString(entity.get().getMultimedia()));
           return response;
        }
      return null;
    }

    @Override
    public void GetMultiPost() {

    }
}
