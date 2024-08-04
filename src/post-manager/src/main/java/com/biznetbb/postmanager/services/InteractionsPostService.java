package com.biznetbb.postmanager.services;

import com.biznetbb.postmanager.models.web.request.CreateCommentRequest;

public interface InteractionsPostService {
    void createComment(CreateCommentRequest request);
}
