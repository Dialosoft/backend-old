package com.biznetbb.postmanager.controller;

import com.biznetbb.postmanager.models.web.request.CreateCommentRequest;
import com.biznetbb.postmanager.services.InteractionsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/post-reaction/")
@RequiredArgsConstructor
public class InteractionPostController {

    private final InteractionsPostService service;
    @PostMapping("/add-comments")
    void postComments(@RequestBody  CreateCommentRequest request){
        service.createComment(request);
    }

}
