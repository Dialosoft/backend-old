package routers

import (
	"github.com/Dialosoft/post-manager-service/src/application/services"
	"github.com/gin-gonic/gin"
)

type CategoryRouter struct {
	CategoryService services.CategoryService
}

func NewCategoryRouter(service services.CategoryService) *CategoryRouter {
	return &CategoryRouter{CategoryService: service}
}

func (r *CategoryRouter) SetupCategoryRoutes() *gin.Engine {
	router := gin.Default()

	manager := router.Group("/management-service")
	categories := manager.Group("/categories")

	categories.GET("")

	return router
}
