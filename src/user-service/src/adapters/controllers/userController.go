package controllers

import (
	"net/http"

	"github.com/Dialosoft/user-service/src/application/services"
	"github.com/Dialosoft/user-service/src/domain/entities/request"
	"github.com/Dialosoft/user-service/src/domain/entities/response"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

func ChangeEmailController(c *gin.Context, userService services.UserService) {
	var req request.Email
	var res response.Standard

	if err := c.Bind(&req); err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "BAD REQUEST"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	userID, err := uuid.Parse(req.UserId)
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "Invalid user ID"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	if err := userService.ChangeEmail(userID, req.NewEmail); err != nil {
		res.StatusCode = http.StatusInternalServerError
		res.Message = err.Error()
		res.Data = nil
		c.JSON(http.StatusInternalServerError, res)
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "Email changed successfully"})
}

func ChangeUserAvatarController(c *gin.Context, userService services.UserService) {
	var res response.Standard

	userID := c.PostForm("userId")
	if userID == "" {
		res.StatusCode = http.StatusBadRequest
		res.Message = "userId cannot be empty"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	avatar, _, err := c.Request.FormFile("avatar")
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "image is required"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}
	defer avatar.Close()

	uuid, err := uuid.Parse(userID)
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "userId is invalid"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	if err := userService.ChangeAvatar(uuid, avatar); err != nil {
		res.StatusCode = http.StatusInternalServerError
		res.Message = err.Error()
		res.Data = nil
		c.JSON(http.StatusInternalServerError, res)
		return
	}

	res.StatusCode = http.StatusOK
	res.Message = "OK"
	res.Data = "avatar changed successfully"
	c.JSON(http.StatusOK, res)
}
