package main

import (
	"fmt"
	"log"

	"github.com/biznetbb/user-service/src/adapters/repositories"
	"github.com/biznetbb/user-service/src/adapters/router"
	"github.com/biznetbb/user-service/src/application/services"
	"github.com/biznetbb/user-service/src/infraestructure/db"
	"github.com/gin-gonic/gin"
)

func main() {
	db, err := db.DBConnection()
	if err != nil {
		log.Fatalf("error initializing database: %v", err)
	}

	fmt.Println("Database connection successful", db)

	userRepo := repositories.NewUserRepository(db)
	userService := services.NewUserService(userRepo)

	r := router.NewRouter(userService)
	router := r.SetupRoutes()

	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	router.Run(":8086")
}
