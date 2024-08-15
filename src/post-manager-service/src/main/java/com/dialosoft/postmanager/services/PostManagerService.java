package com.dialosoft.postmanager.services;

import com.dialosoft.postmanager.models.web.request.PostManagerCommonAttributes;
import com.dialosoft.postmanager.models.web.response.PostManagerResponse;

import java.util.List;

public interface PostManagerService {
    void CreateNewPost(PostManagerCommonAttributes request);
    void DeletePost(String id);
    void ModifiedPost(PostManagerCommonAttributes request);

    PostManagerResponse GetPost(String id);
    List<PostManagerResponse> GetMultiPost(String username);

}
