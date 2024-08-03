package main

import (
	"fmt"
	"log"
	"os"

	"github.com/biznetbb/user-service/src/adapters/repositories"
	"github.com/biznetbb/user-service/src/adapters/router"
	"github.com/biznetbb/user-service/src/application/services"
	"github.com/biznetbb/user-service/src/infraestructure/db"
	"github.com/biznetbb/user-service/src/infraestructure/registry"
	"github.com/gin-gonic/gin"
)

func main() {
	db, err := db.DBConnection()
	if err != nil {
		log.Fatalf("error initializing database: %v", err)
	}

	fmt.Println("Database connection successful", db)

	err = os.MkdirAll("avatars", os.ModePerm)
	if err != nil {
		fmt.Printf("error creating avatars directory: %v\n", err)
	}

	registry.InitEurekaClient()

	userRepo := repositories.NewUserRepository(db)
	userService := services.NewUserService(userRepo)

	r := router.NewRouter(userService)
	router := r.SetupRoutes()

	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	router.Run(":8086")
}
