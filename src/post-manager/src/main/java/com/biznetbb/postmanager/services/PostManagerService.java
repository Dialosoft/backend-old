package com.biznetbb.postmanager.services;

import com.biznetbb.postmanager.models.web.request.PostManagerCommonAttributes;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;

import java.util.List;

public interface PostManagerService {
    void CreateNewPost(PostManagerCommonAttributes request);
    void DeletePost(String id);
    void ModifiedPost(PostManagerCommonAttributes request);

    PostManagerResponse GetPost(String id);
    List<PostManagerResponse> GetMultiPost(String username);

}
