package com.biznetbb.postmanager.services;

import com.biznetbb.postmanager.models.web.request.PostManagerRequest;

public interface PostManagerService {
    void CreateNewPost(PostManagerRequest request);
    void DeletePost();
    void ModifiedPost();

    void GetPost();
    void GetMultiPost();

}
