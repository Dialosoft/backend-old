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

// GetAllCategories godoc
// @Summary Get all categories
// @Description Get a list of all categories
// @Tags categories
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 404 {object} response.Standard "Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /management-service/v1/get-all-categories [get]
func GetAllCategories(c *gin.Context, service services.CategoryService) {
	var res response.Standard

	categories, err := service.GetAllCategories()
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "OK"
	res.Data = categories
	c.JSON(http.StatusOK, res)
}

// GetCategoryByID godoc
// @Summary Get category by ID
// @Description Get a single category by its UUID
// @Tags categories
// @Produce json
// @Param id path string true "Category ID"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /management-service/v1/get-category-id/{id} [get]
func GetCategoryByID(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	categories, err := service.GetCategoryByID(uuid)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "OK"
	res.Data = categories
	c.JSON(http.StatusOK, res)
}

// CreateCategory godoc
// @Summary Create a new category
// @Description Create a new category with a name and description
// @Tags categories
// @Accept json
// @Produce json
// @Param request body request.CreateCategoryRequest true "Category creation data"
// @Success 201 {object} response.Standard
// @Failure 400 {object} response.Standard "Bad Request"
// @Failure 409 {object} response.Standard "Conflict (Duplicate Category)"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /management-service/v1/create-category [post]
func CreateCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	var req request.CreateCategoryRequest

	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	if err := service.CreateCategory(req.Name, req.Description); err != nil {
		if strings.Contains(err.Error(), "duplicate key value") {
			utility.ErrConflict(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res.StatusCode = http.StatusCreated
	res.Message = "CREATED"
	res.Data = nil
	c.JSON(http.StatusCreated, res)
}

// UpdateCategory godoc
// @Summary Update a category by ID
// @Description Update a category's name and description by its UUID
// @Tags categories
// @Accept json
// @Produce json
// @Param id path string true "Category ID"
// @Param request body request.CreateCategoryRequest true "Category update data"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Category Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /management-service/v1/update-category/{id} [put]
func UpdateCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	var req request.CreateCategoryRequest
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	if err := c.BindJSON(&req); err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "BAD REQUEST"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	if err := service.UpdateCategory(uuid, req.Name, req.Description); err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			res.StatusCode = http.StatusInternalServerError
			res.Message = "INTERNAL SERVER ERROR"
			res.Data = nil
			c.JSON(http.StatusInternalServerError, res)
			return
		}
	}
	res.StatusCode = http.StatusOK
	res.Message = "UPDATED"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}

// DeleteCategory godoc
// @Summary Delete a category by ID
// @Description Soft delete a category by its UUID
// @Tags categories
// @Produce json
// @Param id path string true "Category ID"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Category Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /management-service/v1/delete-category/{id} [delete]
func DeleteCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	if err := service.DeleteCategory(uuid); err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "DELETED"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}

// RestoreCategory godoc
// @Summary Restore a soft deleted category by ID
// @Description Restore a category that was previously soft deleted by its UUID
// @Tags categories
// @Produce json
// @Param id path string true "Category ID"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard "Category Not Found"
// @Failure 500 {object} response.Standard "Internal Server Error"
// @Router /management-service/v1/restore-category/{id} [post]
func RestoreCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	if err := service.RestoreCategory(uuid); err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "RESTORED"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}
