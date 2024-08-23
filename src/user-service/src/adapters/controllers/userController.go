package controllers

import (
	"net/http"

	"github.com/Dialosoft/user-service/src/application/services"
	"github.com/Dialosoft/user-service/src/domain/entities/request"
	"github.com/Dialosoft/user-service/src/domain/entities/response"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type GetUserParams struct {
	Username string `json:"username"`
}

// GetUserInfo godoc
// @Summary Get detailed user information
// @Description Get detailed information about a user by passing the "X-Auth-Username" header
// @Tags user
// @Accept  json
// @Produce  json
// @Param X-Auth-Username header string true "Username for the user to retrieve"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid or missing username"
// @Failure 404 {object} response.Standard "User not found"
// @Failure 500 {object} response.Standard "Internal server error"
// @Router /user-service/v1/get-user-info [get]
func GetUserInfo(c *gin.Context, userService services.UserService) {
	var username string
	var res response.Standard

	username = c.GetHeader("X-Auth-Username")
	if username == "" {
		res.StatusCode = http.StatusBadRequest
		res.Message = "BAD REQUEST"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	user, err := userService.GetUser(username)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
			return
		} else {
			res.StatusCode = http.StatusInternalServerError
			res.Message = err.Error()
			res.Data = nil
			c.JSON(http.StatusInternalServerError, res)
			return
		}
	}
	user.Password = ""

	res.Message = "OK"
	res.StatusCode = http.StatusOK
	res.Data = &user
	c.JSON(http.StatusOK, res)
}

// GetSimpleInfo godoc
// @Summary Get simple user information
// @Description Get basic information about a user by passing the "X-Auth-Username" header
// @Tags user
// @Accept  json
// @Produce  json
// @Param X-Auth-Username header string true "Username for the user to retrieve"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid or missing username"
// @Failure 404 {object} response.Standard "User not found"
// @Failure 500 {object} response.Standard "Internal server error"
// @Router /user-service/v1/get-simpleuser-info [get]
func GetSimpleInfo(c *gin.Context, userService services.UserService) {
	var username string
	var res response.Standard

	username = c.GetHeader("X-Auth-Username")
	if username == "" {
		res.StatusCode = http.StatusBadRequest
		res.Message = "BAD REQUEST"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	simpleUser, err := userService.GetSimpleUser(username)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
			return
		} else {
			res.StatusCode = http.StatusInternalServerError
			res.Message = err.Error()
			res.Data = nil
			c.JSON(http.StatusInternalServerError, res)
			return
		}
	}

	res.Message = "OK"
	res.StatusCode = http.StatusOK
	res.Data = &simpleUser
	c.JSON(http.StatusOK, res)
}

// ChangeEmailController godoc
// @Summary Change user email
// @Description Change the email address of a user
// @Tags user
// @Accept  json
// @Produce  json
// @Param request body request.Email true "User email change request"
// @Success 200 {object} response.Standard "Email changed successfully"
// @Failure 400 {object} response.Standard "Invalid request"
// @Failure 404 {object} response.Standard "User not found"
// @Failure 500 {object} response.Standard "Internal server error"
// @Router /user-service/v1/change-email [put]
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
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
		} else {
			res.StatusCode = http.StatusInternalServerError
			res.Message = err.Error()
			res.Data = nil
			c.JSON(http.StatusInternalServerError, res)
		}
		return
	}

	res.StatusCode = http.StatusOK
	res.Message = "Email changed successfully"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}

// ChangeUserAvatarController godoc
// @Summary Change user avatar
// @Description Change the avatar of a user by passing the user ID and an image file
// @Tags user
// @Accept  multipart/form-data
// @Produce  json
// @Param userId formData string true "User ID"
// @Param avatar formData file true "Avatar image file"
// @Success 200 {object} response.Standard "Avatar changed successfully"
// @Failure 400 {object} response.Standard "Invalid request or missing fields"
// @Failure 500 {object} response.Standard "Internal server error"
// @Router /user-service/v1/change-avatar [put]
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
	res.Message = "avatar changed successfully"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}
