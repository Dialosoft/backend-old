package utility

import (
	"net/http"

	"github.com/Dialosoft/management-service/src/domain/entities/response"
	"github.com/gin-gonic/gin"
)

func ErrBadRequest(c *gin.Context) {
	var res response.Standard
	res.StatusCode = http.StatusBadRequest
	res.Message = "BAD REQUEST"
	res.Data = nil
	c.JSON(http.StatusBadRequest, res)
}

func ErrNotFound(c *gin.Context) {
	var res response.Standard
	res.StatusCode = http.StatusNotFound
	res.Message = "NOT FOUND"
	res.Data = nil
	c.JSON(http.StatusNotFound, res)
}

func ErrInvalidUUID(c *gin.Context) {
	var res response.Standard
	res.StatusCode = http.StatusBadRequest
	res.Message = "UUID INVALID"
	res.Data = nil
	c.JSON(http.StatusBadRequest, res)
}

func ErrConflict(c *gin.Context) {
	var res response.Standard
	res.StatusCode = http.StatusConflict
	res.Message = "CONFLICT"
	res.Data = nil
	c.JSON(http.StatusConflict, res)
}

func ErrInternalServer(c *gin.Context) {
	var res response.Standard
	res.StatusCode = http.StatusInternalServerError
	res.Message = "INTERNAL SERVER ERROR"
	res.Data = nil
	c.JSON(http.StatusInternalServerError, res)
}
