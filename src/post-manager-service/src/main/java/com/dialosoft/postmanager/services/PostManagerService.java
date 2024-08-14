package com.dialosoft.postmanager.services;

import com.dialosoft.postmanager.models.web.request.PostManagerRequest;
import com.dialosoft.postmanager.models.web.response.PostManagerResponse;

import java.util.List;

public interface PostManagerService {
    void CreateNewPost(PostManagerRequest request);
    void DeletePost(String id);
    void ModifiedPost(PostManagerRequest request);

    PostManagerResponse GetPost(String id);
    List<PostManagerResponse> GetMultiPost(String username);

}
