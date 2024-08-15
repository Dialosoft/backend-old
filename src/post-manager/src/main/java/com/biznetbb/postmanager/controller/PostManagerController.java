package com.biznetbb.postmanager.controller;

import com.biznetbb.postmanager.models.web.request.PostManagerCommonAttributes;
import com.biznetbb.postmanager.models.web.response.PostManagerResponse;
import com.biznetbb.postmanager.services.PostManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/v1/post-manager/")
@RequiredArgsConstructor
public class PostManagerController {

    private final PostManagerService service; // all injections of dependencies must be with constructor

    @PostMapping("create-post")
    public void createPost(@RequestBody PostManagerCommonAttributes request) {
        service.CreateNewPost(request);
    }

    @DeleteMapping("delete-post/{id}")
    public void deletePost(@PathVariable String id) {

        service.DeletePost(id);
    }

    @PutMapping("modify-post")
    public void modifyPost(@RequestBody PostManagerCommonAttributes request) {
        service.ModifiedPost(request);
    }

    @GetMapping("get-post/{id}")
    public PostManagerResponse getSinglePost(@PathVariable String id) {
       return service.GetPost(id);
    }

    @GetMapping("get-post-from-id")
    public List<PostManagerResponse> getAllPostFromUser(@RequestParam String username) {
       return service.GetMultiPost(username);
    }


}
