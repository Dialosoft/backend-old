package routers

import (
	"github.com/Dialosoft/management-service/src/adapters/controllers"
	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/gin-gonic/gin"
)

type RoleRouter struct {
	RoleService services.RoleService
}

func NewRoleRouter(service services.RoleService) *RoleRouter {
	return &RoleRouter{RoleService: service}
}

func (r *RoleRouter) SetupRolesRoutes(group *gin.RouterGroup) {
	roleGroup := group.Group("/roles")
	{
		roleGroup.GET("/get-all-roles", r.getAllRolesHandle)
		roleGroup.GET("/get-role-by-id/:id", r.getRoleByIdHandle)
		roleGroup.GET("/get-role-by-type/:roleType", r.getRoleByTypeHandle)
		roleGroup.POST("/create-role", r.CreateRoleHandle)
		roleGroup.PUT("/update-role", r.UpdateRoleHandle)
		roleGroup.DELETE("/delete-role/:id", r.UpdateRoleHandle)
		roleGroup.POST("/restore-role/:id", r.RestoreRoleHandle)
		roleGroup.PUT("/change-user-role", r.ChangeUserRoleHandle)
	}
}

func (r *RoleRouter) getAllRolesHandle(c *gin.Context) {
	controllers.GetAllRoles(c, r.RoleService)
}

func (r *RoleRouter) getRoleByIdHandle(c *gin.Context) {
	controllers.GetRoleByID(c, r.RoleService)
}

func (r *RoleRouter) getRoleByTypeHandle(c *gin.Context) {
	controllers.GetRoleByType(c, r.RoleService)
}

func (r *RoleRouter) CreateRoleHandle(c *gin.Context) {
	controllers.CreateRole(c, r.RoleService)
}

func (r *RoleRouter) UpdateRoleHandle(c *gin.Context) {
	controllers.UpdateRole(c, r.RoleService)
}

func (r *RoleRouter) DeleteRoleHandle(c *gin.Context) {
	controllers.DeleteRole(c, r.RoleService)
}

func (r *RoleRouter) RestoreRoleHandle(c *gin.Context) {
	controllers.RestoreRole(c, r.RoleService)
}

func (r *RoleRouter) ChangeUserRoleHandle(c *gin.Context) {
	controllers.ChangeUserRole(c, r.RoleService)
}
