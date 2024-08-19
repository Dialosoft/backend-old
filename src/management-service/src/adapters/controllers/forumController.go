package controllers

import (
	"net/http"
	"strings"

	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/Dialosoft/management-service/src/domain/entities/request"
	"github.com/Dialosoft/management-service/src/domain/entities/response"
	"github.com/Dialosoft/management-service/src/utility"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

// GetAllForums godoc
// @Summary Get all forums
// @Description Get a list of all forums
// @Tags forums
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 404 {object} response.Standard "Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /get-all-forums [get]
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

// GetForumByID godoc
// @Summary Get forum by ID
// @Description Get a single forum by its UUID
// @Tags forums
// @Produce json
// @Param id path string true "Forum ID"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /get-forum-id/{id} [get]
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

// CreateForum godoc
// @Summary Create a new forum
// @Description Create a new forum with a name, description, and category ID
// @Tags forums
// @Accept json
// @Produce json
// @Param request body request.CreateForum true "Forum creation data"
// @Success 201 {object} response.Standard
// @Failure 400 {object} response.Standard "Bad Request"
// @Failure 404 {object} response.Standard "Category Not Found"
// @Failure 409 {object} response.Standard "Conflict (Duplicate Forum)"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /create-forum [post]
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
		} else if strings.Contains(err.Error(), "duplicate key value") {
			utility.ErrConflict(c)
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

// UpdateForumCategoryOwner godoc
// @Summary Update the category owner of a forum
// @Description Update the category of a forum by its UUID and new category ID
// @Tags forums
// @Accept json
// @Produce json
// @Param request body request.ForumUpdateCategoryOwner true "Forum and Category IDs"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Forum or Category Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /update-forum-category [put]
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

// UpdateForumName godoc
// @Summary Update the name of a forum
// @Description Update the name of a forum by its UUID
// @Tags forums
// @Accept json
// @Produce json
// @Param request body request.ForumUpdateName true "Forum name update data"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Forum Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /update-forum-name [put]
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

// UpdateForumDescription godoc
// @Summary Update the description of a forum
// @Description Update the description of a forum by its UUID
// @Tags forums
// @Accept json
// @Produce json
// @Param request body request.ForumUpdateDescription true "Forum description update data"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Forum Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /update-forum-description [put]
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

// DeleteForum godoc
// @Summary Delete a forum
// @Description Soft delete a forum by its UUID
// @Tags forums
// @Produce json
// @Param id path string true "Forum ID"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Forum Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /delete-forum/{id} [delete]
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

// RestoreForum godoc
// @Summary Restore a soft deleted forum
// @Description Restore a forum that was previously soft deleted by its UUID
// @Tags forums
// @Produce json
// @Param id path string true "Forum ID"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Forum Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /restore-forum/{id} [post]
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
