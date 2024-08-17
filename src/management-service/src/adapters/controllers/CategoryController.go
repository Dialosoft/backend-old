package controllers

import (
	"net/http"

	"github.com/Dialosoft/post-manager-service/src/application/services"
	"github.com/Dialosoft/post-manager-service/src/domain/entities/request"
	"github.com/Dialosoft/post-manager-service/src/domain/entities/response"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

func GetAllCategories(c *gin.Context, service services.CategoryService) {
	var res response.Standard

	categories, err := service.GetAllCategories()
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "OK"
	res.Data = categories
	c.JSON(http.StatusOK, res)
}

func GetCategoryByID(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "UUID INVALID"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	categories, err := service.GetCategoryByID(uuid)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "OK"
	res.Data = categories
	c.JSON(http.StatusOK, res)
}

func CreateCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	var req request.CreateCategoryRequest

	if err := c.BindJSON(&req); err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "BAD REQUEST"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	if err := service.CreateCategory(req.Name, req.Description); err != nil {
		if err == gorm.ErrDuplicatedKey {
			res.StatusCode = http.StatusConflict
			res.Message = "CONFLICT"
			res.Data = nil
			c.JSON(http.StatusConflict, res)
			return
		} else {
			res.StatusCode = http.StatusInternalServerError
			res.Message = "INTERNAL SERVER ERROR"
			res.Data = nil
			c.JSON(http.StatusInternalServerError, res)
			return
		}
	}

	res.StatusCode = http.StatusCreated
	res.Message = "CREATED"
	res.Data = nil
	c.JSON(http.StatusCreated, res)
}

func UpdateCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	var req request.CreateCategoryRequest
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "UUID INVALID"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
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
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
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

func DeleteCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "UUID INVALID"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	if err := service.DeleteCategory(uuid); err != nil {
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
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
	res.Message = "DELETED"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}

func RestoreCategory(c *gin.Context, service services.CategoryService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		res.StatusCode = http.StatusBadRequest
		res.Message = "UUID INVALID"
		res.Data = nil
		c.JSON(http.StatusBadRequest, res)
		return
	}

	if err := service.RestoreCategory(uuid); err != nil {
		if err == gorm.ErrRecordNotFound {
			res.StatusCode = http.StatusNotFound
			res.Message = "NOT FOUND"
			res.Data = nil
			c.JSON(http.StatusNotFound, res)
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
	res.Message = "RESTORED"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}
