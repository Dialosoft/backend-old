package com.dialosoft.postmanager.controller;

import com.dialosoft.postmanager.models.web.request.CreateCommentRequest;
import com.dialosoft.postmanager.services.InteractionsPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/post-reaction/")
@Tag(name = "Post Reactions", description = "Operations related to reactions on posts, such as adding comments")
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
    public void postComments(@RequestBody CreateCommentRequest request) {
        service.createComment(request);
    }
}
