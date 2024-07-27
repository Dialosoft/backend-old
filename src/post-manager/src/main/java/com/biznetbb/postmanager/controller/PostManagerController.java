package com.biznetbb.postmanager.controller;

import com.biznetbb.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/v1/post-manager/")
@RequiredArgsConstructor
public class PostManagerController {

    private final PostManagerService service; // all injections of dependencies must be with constructor
    @PostMapping("create-post")
    public void createPost(){
        service.CreateNewPost();
    }

    @DeleteMapping("delete-post/{id}")
    public void deletePost(@RequestParam String id){
        service.DeletePost();
    }
}
