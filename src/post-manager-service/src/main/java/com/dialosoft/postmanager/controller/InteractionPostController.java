package com.dialosoft.postmanager.controller;

import com.dialosoft.postmanager.models.web.request.CreateCommentCommonAttributes;
import com.dialosoft.postmanager.models.web.request.CreateReactionCommonAttributes;
import com.dialosoft.postmanager.services.InteractionsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/post-reaction/")
@RequiredArgsConstructor
public class InteractionPostController {

    private final InteractionsPostService service;
    @PostMapping("/add-comments")
    void postComments(@RequestBody CreateCommentCommonAttributes request){
        service.createComment(request);
    }

    @PutMapping("/modify-comment")
    void modifyComments(@RequestBody CreateCommentCommonAttributes request){
        service.modifyComment(request);
    }
    @DeleteMapping("/delete-comment")
    void deleteComments(@RequestParam String id){
        service.deleteComment(id);
    }

    @PostMapping("/create-reaction")
    void postReaction(@RequestBody CreateReactionCommonAttributes request){
        service.createReaction(request);
    }

    @PutMapping("/modify-reaction")
    void modifyReaction(@RequestBody CreateCommentCommonAttributes request){
        service.modifyComment(request);
    }
    @DeleteMapping("/delete-reaction")
    void deleteReaction(){
    }

}
