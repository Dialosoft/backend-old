package controllers

import (
	"net/http"

	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/Dialosoft/management-service/src/domain/entities/request"
	"github.com/Dialosoft/management-service/src/domain/entities/response"
	"github.com/Dialosoft/management-service/src/utility"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

func GetAllForums(c *gin.Context, service services.ForumService) {
	forums, err := service.GetAllForums()
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "OK",
		Data:       forums,
	}
	c.JSON(http.StatusOK, res)
}

func GetForumByID(c *gin.Context, service services.ForumService) {
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	forum, err := service.GetForumByID(uuid)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "OK",
		Data:       forum,
	}

	c.JSON(http.StatusOK, res)
}

func CreateForum(c *gin.Context, service services.ForumService) {
	var req request.CreateForum

	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	if req.Name == "" || req.Description == "" || req.CategoryID == "" {
		utility.ErrBadRequest(c)
		return
	}

	uuid, err := uuid.Parse(req.CategoryID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	err = service.CreateForum(req.Name, req.Description, uuid)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusCreated,
		Message:    "CREATED",
		Data:       nil,
	}

	c.JSON(http.StatusCreated, res)
}

func UpdateForumCategoryOwner(c *gin.Context, service services.ForumService) {
	var req request.ForumUpdateCategoryOwner
	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	forumUUID, err := uuid.Parse(req.ForumID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	categoryUUD, err := uuid.Parse(req.CategoryID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	err = service.UpdateForumCategoryOwner(forumUUID, categoryUUD)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "UPDATED",
		Data:       nil,
	}

	c.JSON(http.StatusOK, res)
}

func UpdateForumName(c *gin.Context, service services.ForumService) {
	var req request.ForumUpdateName
	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	uuid, err := uuid.Parse(req.ForumID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	err = service.UpdateForumName(uuid, req.Name)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "UPDATED",
		Data:       nil,
	}

	c.JSON(http.StatusOK, res)
}

func UpdateForumDescription(c *gin.Context, service services.ForumService) {
	var req request.ForumUpdateDescription
	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	uuid, err := uuid.Parse(req.ForumID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	err = service.UpdateForumDescription(uuid, req.Description)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "UPDATED",
		Data:       nil,
	}

	c.JSON(http.StatusOK, res)
}

func DeleteForum(c *gin.Context, service services.ForumService) {
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	err = service.DeleteForum(uuid)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "DELETED",
		Data:       nil,
	}
	c.JSON(http.StatusOK, res)
}

func RestoreForum(c *gin.Context, service services.ForumService) {
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	err = service.RestoreForum(uuid)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res := response.Standard{
		StatusCode: http.StatusOK,
		Message:    "RESTORED",
		Data:       nil,
	}
	c.JSON(http.StatusOK, res)
}
