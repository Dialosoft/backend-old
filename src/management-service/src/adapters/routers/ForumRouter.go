package routers

import (
	"github.com/Dialosoft/post-manager-service/src/application/services"
	"github.com/gin-gonic/gin"
)

type ForumRouter struct {
	ForumService services.ForumService
}

func NewForumRouter(service services.ForumService) *ForumRouter {
	return &ForumRouter{ForumService: service}
}

func (r *ForumRouter) SetupForumRoutes() *gin.Engine {
	router := gin.Default()

	manager := router.Group("/management-service")
	forums := manager.Group("/forums")

	// Routes

	forums.GET("")

	return router
}
