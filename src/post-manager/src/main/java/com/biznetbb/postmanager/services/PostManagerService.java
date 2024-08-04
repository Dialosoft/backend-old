package com.biznetbb.postmanager.services;

import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;

public interface PostManagerService {
    void CreateNewPost(PostManagerRequest request);
    void DeletePost();
    void ModifiedPost(PostManagerRequest request);

    PostManagerResponse GetPost(String id);
    void GetMultiPost();

}
