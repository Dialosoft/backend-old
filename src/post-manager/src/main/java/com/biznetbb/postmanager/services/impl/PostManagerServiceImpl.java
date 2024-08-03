package com.biznetbb.postmanager.services.impl;

import com.biznetbb.postmanager.models.entities.PostEntity;
import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import com.biznetbb.postmanager.repository.PostManagerRepository;
import com.biznetbb.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostManagerServiceImpl implements PostManagerService {

    private  final PostManagerRepository postManagerRepository;
    @Override
    public void CreateNewPost(PostManagerRequest request) {
        PostEntity entity = new PostEntity();
        if (request != null){
            //entity.setComments(request.getComments());
            entity.setContent(request.getContent());
            entity.setMultimedia(request.getImage());
            postManagerRepository.save(entity);
        }
    }

    @Override
    public void DeletePost() {

    }

    @Override
    public void ModifiedPost() {

    }

    @Override
    public void GetPost() {

    }

    @Override
    public void GetMultiPost() {

    }
}
