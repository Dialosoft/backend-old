package com.dialosoft.postmanager.controller;

import com.dialosoft.postmanager.models.web.request.PostManagerCommonAttributes;
import com.dialosoft.postmanager.models.web.request.SavePostAsFavoriteFromUserRequest;
import com.dialosoft.postmanager.models.web.response.PostManagerResponse;
import com.dialosoft.postmanager.services.PostManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/post-manager")
@Tag(name = "Post Management", description = "Operations related to managing posts")
@RequiredArgsConstructor
public class PostManagerController {

    private final PostManagerService service;

    @Operation(
            summary = "Create a new post",
            description = "Creates a new post based on the provided request data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PostMapping("create-post")
    public void createPost(@RequestBody PostManagerCommonAttributes request) {
        service.CreateNewPost(request);
    }

    @Operation(
            summary = "Delete an existing post",
            description = "Deletes a post identified by the given ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @DeleteMapping("delete-post/{id}")
    public void deletePost(@PathVariable String id) {

        service.DeletePost(id);
    }

    @Operation(
            summary = "Modify an existing post",
            description = "Modifies a post based on the provided request data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PutMapping("modify-post")
    public void modifyPost(@RequestBody PostManagerCommonAttributes request) {
        service.ModifiedPost(request);
    }

    @Operation(
            summary = "Get a single post by ID",
            description = "Retrieves the details of a post identified by the given ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostManagerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @GetMapping("get-post/{id}")
    public PostManagerResponse getSinglePost(@PathVariable String id) {
       return service.GetPost(id);
    }

    @Operation(
            summary = "Get all posts by username",
            description = "Retrieves all posts created by the user identified by the given username.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostManagerResponse.class))),
            @ApiResponse(responseCode = "404", description = "No posts found for the given username", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @GetMapping("get-post-from-user")
    public List<PostManagerResponse> getAllPostFromUser(@RequestParam String username) {
       return service.GetMultiPost(username);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostManagerResponse.class))),
            @ApiResponse(responseCode = "404", description = "No posts found for the given forumId", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @GetMapping("get-post-from-forum")
    public List<PostManagerResponse> getAllPostFromForum(@RequestParam String forumId) {
        return service.GetMultiPostFromForum(forumId);
    }

    @Operation(
            summary = "Get the saved as favorites post from a user ",
            description = "fetch the favorites post identified by the given ID of the user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostManagerResponse.class))),
            @ApiResponse(responseCode = "404", description = "No posts found for the given forumId", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @GetMapping("get-favorites-post")
    public List<PostManagerResponse> getAllSavePostFromUsername(@RequestParam String username) {
        return service.getFavoritesPostFromUser(username);
    }

    @Operation(
            summary = "Save post as favorite",
            description = "Save o modified the post as favorite",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post save successfully as favorite"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PostMapping("add-favorite-post")
    public void savePostAsFavoriteFromUser(@RequestBody SavePostAsFavoriteFromUserRequest request){
           service.savePostAsFavorite(request.getPostId(),request.getIsFavorite());
    }

}
