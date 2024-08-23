package routers

import (
	"github.com/Dialosoft/management-service/src/adapters/controllers"
	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/gin-gonic/gin"
)

type CategoryRouter struct {
	CategoryService services.CategoryService
}

func NewCategoryRouter(service services.CategoryService) *CategoryRouter {
	return &CategoryRouter{CategoryService: service}
}

func (r *CategoryRouter) SetupCategoryRoutes(group *gin.RouterGroup) {
	categoryGroup := group.Group("/categories")
	{
		categoryGroup.GET("/get-all-categories", r.getAllCategoryHandle)
		categoryGroup.GET("/get-category-id/:id", r.getCategoryByIdHandle)
		categoryGroup.POST("/create-category", r.createCategoryHandle)
		categoryGroup.PUT("/update-category/:id", r.updateCategoryHandle)
		categoryGroup.DELETE("/delete-category/:id", r.deleteCategoryHandle)
		categoryGroup.POST("/restore-category/:id", r.restoreCategoryHandle)
	}
}

func (r *CategoryRouter) getAllCategoryHandle(c *gin.Context) {
	controllers.GetAllCategories(c, r.CategoryService)
}

func (r *CategoryRouter) getCategoryByIdHandle(c *gin.Context) {
	controllers.GetCategoryByID(c, r.CategoryService)
}

func (r *CategoryRouter) createCategoryHandle(c *gin.Context) {
	controllers.CreateCategory(c, r.CategoryService)
}

func (r *CategoryRouter) updateCategoryHandle(c *gin.Context) {
	controllers.UpdateCategory(c, r.CategoryService)
}

func (r *CategoryRouter) deleteCategoryHandle(c *gin.Context) {
	controllers.DeleteCategory(c, r.CategoryService)
}

func (r *CategoryRouter) restoreCategoryHandle(c *gin.Context) {
	controllers.RestoreCategory(c, r.CategoryService)
}
