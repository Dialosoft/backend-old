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

// GetAllRoles godoc
// @Summary Get all roles
// @Description Get all the roles available in the system
// @Tags roles
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 404 {object} response.Standard
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/get-all-roles [get]
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

// GetRoleByID godoc
// @Summary Get a role by ID
// @Description Get details of a role using its UUID
// @Tags roles
// @Param id path string true "Role UUID"
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/get-role-by-id/{id} [get]
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

// GetRoleByType godoc
// @Summary Get a role by type
// @Description Get details of a role by its type (e.g., admin, user)
// @Tags roles
// @Param roleType path string true "Role Type"
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 404 {object} response.Standard
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/get-role-by-type/{roleType} [get]
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

// CreateRole godoc
// @Summary Create a new role
// @Description Create a new role with the specified permissions
// @Tags roles
// @Accept json
// @Produce json
// @Param role body request.CreateRoleRequest true "Role data"
// @Success 201 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid request body"
// @Failure 409 {object} response.Standard "Duplicate role"
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/create-role [post]
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

// UpdateRole godoc
// @Summary Update a role
// @Description Update an existing role by its UUID
// @Tags roles
// @Param id path string true "Role UUID"
// @Accept json
// @Produce json
// @Success 501 {object} response.Standard "Not implemented"
// @Router /management-service/v1/update-role [put]
func UpdateRole(c *gin.Context, service services.RoleService) {
	c.JSON(http.StatusNotImplemented, nil)
}

// DeleteRole godoc
// @Summary Delete a role
// @Description Delete an existing role by its UUID
// @Tags roles
// @Param id path string true "Role UUID"
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/delete-role/{id} [delete]
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

// RestoreRole godoc
// @Summary Restore a deleted role
// @Description Restore a previously deleted role by its UUID
// @Tags roles
// @Param id path string true "Role UUID"
// @Produce json
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid UUID"
// @Failure 404 {object} response.Standard
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/restore-role/{id} [post]
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

// ChangeUserRole godoc
// @Summary Change a user's role
// @Description Change the role of a specific user
// @Tags roles
// @Accept json
// @Produce json
// @Param userRole body request.ChangeUserRole true "User and Role data"
// @Success 200 {object} response.Standard
// @Failure 400 {object} response.Standard "Invalid request body or UUID"
// @Failure 404 {object} response.Standard
// @Failure 500 {object} response.Standard
// @Router /management-service/v1/change-user-role [put]
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
