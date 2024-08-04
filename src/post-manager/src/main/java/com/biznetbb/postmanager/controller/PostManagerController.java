package com.biznetbb.postmanager.controller;

import com.biznetbb.postmanager.models.web.request.PostManagerRequest;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;
import com.biznetbb.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/post-manager/")
@RequiredArgsConstructor
public class PostManagerController {

    private final PostManagerService service; // all injections of dependencies must be with constructor

    @PostMapping("create-post")
    public void createPost(@RequestBody PostManagerRequest request) {
        service.CreateNewPost(request);
    }

    @DeleteMapping("delete-post/{id}")
    public void deletePost(@RequestParam String id) {
        service.DeletePost();
    }

    @PutMapping("modify-post")
    public void modifyPost(@RequestBody PostManagerRequest request) {
        service.ModifiedPost(request);
    }

    @GetMapping("get-post/{id}")
    public PostManagerResponse getSinglePost(@PathVariable String id) {
       return service.GetPost(id);
    }

    @GetMapping("get-post-from-id/{id}")
    public void getAllPostFromUser(@RequestParam Long id) {
        service.GetMultiPost();
    }


}
