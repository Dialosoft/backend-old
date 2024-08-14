package com.dialosoft.postmanager.services;

import com.dialosoft.postmanager.models.web.request.CreateCommentRequest;

public interface InteractionsPostService {
    void createComment(CreateCommentRequest request);
}
