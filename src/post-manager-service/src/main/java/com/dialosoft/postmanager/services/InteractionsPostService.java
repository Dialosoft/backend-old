package com.dialosoft.postmanager.services;

import com.dialosoft.postmanager.models.dto.Comments;
import com.dialosoft.postmanager.models.web.request.CreateCommentCommonAttributes;
import com.dialosoft.postmanager.models.web.request.CreateReactionCommonAttributes;

import java.util.List;

public interface InteractionsPostService {
    void createComment(CreateCommentCommonAttributes request);
    void modifyComment(CreateCommentCommonAttributes request);
    void deleteComment(String id);
    List<Comments> getAllCommentsFromPost(String id);

    void createReaction(CreateReactionCommonAttributes request);
    void modifyReaction(CreateReactionCommonAttributes request);
    void deleteReaction(String id);

}
