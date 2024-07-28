package main

import (
	"github.com/biznetbb/user-service/src/adapters/controllers"
	"github.com/gin-gonic/gin"
)

func main() {
	router := gin.Default()

	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	initializeRoutes(router)

	router.Run(":8086")
}

func initializeRoutes(router *gin.Engine) {
	userRoutes := router.Group("/biznetbb-api/user-service")

	userRoutes.GET("/", controllers.Home)
}
