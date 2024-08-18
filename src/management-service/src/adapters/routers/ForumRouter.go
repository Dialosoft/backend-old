package routers

import (
	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/gin-gonic/gin"
)

type ForumRouter struct {
	ForumService services.ForumService
}

func NewForumRouter(service services.ForumService) *ForumRouter {
	return &ForumRouter{ForumService: service}
}

func (r *ForumRouter) SetupForumRoutes(group *gin.RouterGroup) {
	forumGroup := group.Group("/forums")
	{
		forumGroup.GET("/test", r.Test)
	}
}

func (r *ForumRouter) Test(c *gin.Context) {
	c.JSON(200, gin.H{"message": "ok"})
}
