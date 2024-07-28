package main

import (
	"fmt"
	"log"

	"github.com/biznetbb/user-service/src/adapters/controllers"
	"github.com/biznetbb/user-service/src/infraestructure/db"
	"github.com/gin-gonic/gin"
)

func main() {
	db, err := db.DBConnection()
	if err != nil {
		log.Fatalf("error initializing database: %v", err)
	}

	fmt.Println("Database connection successful", db)

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
