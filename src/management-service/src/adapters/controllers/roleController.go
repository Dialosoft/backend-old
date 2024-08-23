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

func GetAllRoles(c *gin.Context, service services.RoleService) {
	var res response.Standard

	roles, err := service.GetAllRoles()
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
	res.Data = roles
	c.JSON(http.StatusOK, res)
}
func GetRoleByID(c *gin.Context, service services.RoleService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	role, err := service.GetRoleByID(uuid)
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
	res.Data = role
	c.JSON(http.StatusOK, res)
}

func GetRoleByType(c *gin.Context, service services.RoleService) {
	var res response.Standard
	roleType := c.Param("roleType")

	role, err := service.GetRoleByType(roleType)
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
	res.Data = role
	c.JSON(http.StatusOK, res)
}
func CreateRole(c *gin.Context, service services.RoleService) {
	var res response.Standard
	var req request.CreateRoleRequest

	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	if err := service.CreateRole(req.RoleType, req.Permission, req.AdminRole, req.ModRole); err != nil {
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
func UpdateRole(c *gin.Context, service services.RoleService) {
	c.JSON(http.StatusNotImplemented, nil)
}
func DeleteRole(c *gin.Context, service services.RoleService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	if err := service.DeleteRole(uuid); err != nil {
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
func RestoreRole(c *gin.Context, service services.RoleService) {
	var res response.Standard
	id := c.Param("id")

	uuid, err := uuid.Parse(id)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	if err := service.RestoreRole(uuid); err != nil {
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

func ChangeUserRole(c *gin.Context, service services.RoleService) {
	var res response.Standard
	var req request.ChangeUserRole

	if err := c.BindJSON(&req); err != nil {
		utility.ErrBadRequest(c)
		return
	}

	roleUUID, err := uuid.Parse(req.RoleID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	userUUID, err := uuid.Parse(req.UserID)
	if err != nil {
		utility.ErrInvalidUUID(c)
		return
	}

	if err := service.ChangeUserRole(roleUUID, userUUID); err != nil {
		if err == gorm.ErrRecordNotFound {
			utility.ErrNotFound(c)
			return
		} else {
			utility.ErrInternalServer(c)
			return
		}
	}

	res.StatusCode = http.StatusOK
	res.Message = "UPDATED"
	res.Data = nil
	c.JSON(http.StatusOK, res)
}
