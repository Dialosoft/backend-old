package controllers

import (
	"net/http"

	"github.com/Dialosoft/user-service/src/application/services"
	"github.com/Dialosoft/user-service/src/domain/entities/request"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

func ChangeEmailController(c *gin.Context, userService services.UserService) {
	var req request.Email

	if err := c.Bind(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	userID, err := uuid.Parse(req.UserId)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid user ID"})
		return
	}

	if err := userService.ChangeEmail(userID, req.NewEmail); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "Email changed successfully"})
}

func ChangeUserAvatarController(c *gin.Context, userService services.UserService) {

	userID := c.PostForm("userID")
	if userID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "userId cannot be empty"})
		return
	}

	avatar, _, err := c.Request.FormFile("avatar")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "image is required"})
		return
	}
	defer avatar.Close()

	uuid, err := uuid.Parse(userID)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "userId is invalid"})
		return
	}

	if err := userService.ChangeAvatar(uuid, avatar); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "avatar changed successfully"})
}
