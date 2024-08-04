package router

import (
	"github.com/biznetbb/user-service/src/adapters/controllers"
	"github.com/biznetbb/user-service/src/application/services"
	"github.com/gin-gonic/gin"
)

type Router struct {
	UserService services.UserService
}

func NewRouter(userService services.UserService) *Router {
	return &Router{UserService: userService}
}

func (r *Router) SetupRoutes() *gin.Engine {
	router := gin.Default()

	userService := router.Group("/biznetbb-api/user-service")

	// Routes
	userService.Static("/avatars", "./avatars")
	userService.PUT("/change-email", r.handleChangeEmail)
	userService.PUT("/change-avatar", r.handleChangeAvatar)

	return router
}

func (r *Router) handleChangeEmail(c *gin.Context) {
	controllers.ChangeEmailController(c, r.UserService)
}

func (r *Router) handleChangeAvatar(c *gin.Context) {
	controllers.ChangeUserAvatarController(c, r.UserService)
}
