package routers

import (
	"github.com/Dialosoft/management-service/src/adapters/controllers"
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
		forumGroup.GET("/get-all-forums", r.getAllForumsHandle)
		forumGroup.GET("/get-forum-id/:id", r.getForumByIdHandle)
		forumGroup.POST("/create-forum", r.createForumHandle)
		forumGroup.PUT("/update-forum-category", r.updateForumCategoryOwnerHandle)
		forumGroup.PUT("/update-forum-name", r.updateForumNameHandle)
		forumGroup.PUT("/update-forum-description", r.updateForumDescriptionHandle)
		forumGroup.DELETE("/delete-forum/:id", r.deleteForumHandleHandle)
		forumGroup.POST("/restore-forum/:id", r.restoreForumHandle)
	}
}

func (r *ForumRouter) getAllForumsHandle(c *gin.Context) {
	controllers.GetAllForums(c, r.ForumService)
}

func (r *ForumRouter) getForumByIdHandle(c *gin.Context) {
	controllers.GetForumByID(c, r.ForumService)
}
func (r *ForumRouter) createForumHandle(c *gin.Context) {
	controllers.CreateForum(c, r.ForumService)
}
func (r *ForumRouter) updateForumCategoryOwnerHandle(c *gin.Context) {
	controllers.UpdateForumCategoryOwner(c, r.ForumService)
}
func (r *ForumRouter) updateForumNameHandle(c *gin.Context) {
	controllers.UpdateForumName(c, r.ForumService)
}
func (r *ForumRouter) updateForumDescriptionHandle(c *gin.Context) {
	controllers.UpdateForumDescription(c, r.ForumService)
}
func (r *ForumRouter) deleteForumHandleHandle(c *gin.Context) {
	controllers.DeleteForum(c, r.ForumService)
}
func (r *ForumRouter) restoreForumHandle(c *gin.Context) {
	controllers.RestoreForum(c, r.ForumService)
}
