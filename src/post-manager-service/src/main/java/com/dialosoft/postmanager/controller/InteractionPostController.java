package com.dialosoft.postmanager.controller;

import com.dialosoft.postmanager.models.web.request.CreateCommentCommonAttributes;
import com.dialosoft.postmanager.models.web.request.CreateReactionCommonAttributes;
import com.dialosoft.postmanager.services.InteractionsPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/post-reaction/")
@Tag(name = "Post Reactions", description = "Operations for managing comments and reactions on posts")
@RequiredArgsConstructor
public class InteractionPostController {

    private final InteractionsPostService service;

    @Operation(
            summary = "Add a comment to a post",
            description = "Adds a comment to a specified post based on the provided request data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PostMapping("/add-comments")
    public void postComments(@RequestBody CreateCommentCommonAttributes request) {
        service.createComment(request);
    }

    @Operation(
            summary = "Modify an existing comment",
            description = "Modifies an existing comment based on the provided request data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or comment not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PutMapping("/modify-comment")
    public void modifyComments(@RequestBody CreateCommentCommonAttributes request) {
        service.modifyComment(request);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Deletes an existing comment based on the provided comment ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment ID or comment not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @DeleteMapping("/delete-comment")
    public void deleteComments(@RequestParam String id) {
        service.deleteComment(id);
    }

    @Operation(
            summary = "Add a reaction to a post or comment",
            description = "Adds a reaction (like or dislike) to a specified post or comment.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reaction added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PostMapping("/create-reaction")
    public void postReaction(@RequestBody CreateReactionCommonAttributes request) {
        service.createReaction(request);
    }

    @Operation(
            summary = "Modify an existing reaction",
            description = "Modifies an existing reaction based on the provided request data.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reaction modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or reaction not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @PutMapping("/modify-reaction")
    public void modifyReaction(@RequestBody CreateCommentCommonAttributes request) {
        service.modifyComment(request);
    }

    @Operation(
            summary = "Delete a reaction",
            description = "Deletes an existing reaction based on the provided reaction ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reaction deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reaction ID or reaction not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content)
    })
    @DeleteMapping("/delete-reaction")
    public void deleteReaction(@RequestParam String id) {
        service.deleteReaction(id);
    }
}