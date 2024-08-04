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
	EUREKAURL := "http://registry-service:8761/eureka" //os.Getenv("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE")
	appName := "user-service"
	hostname := "localhost"
	ipAdrr := "172.19.0.3"
	port := 8086

	db, err := db.DBConnection()
	if err != nil {
		log.Fatalf("error initializing database: %v", err)
	}

	fmt.Println("Database connection successful", db)

	err = os.MkdirAll("avatars", os.ModePerm)
	if err != nil {
		fmt.Printf("error creating avatars directory: %v\n", err)
	}

	client := registry.NewEurekaClient(EUREKAURL, appName, hostname, ipAdrr, port)

	if err := client.Register(); err != nil {
		log.Fatalf("Failed to register service: %v", err)
	}

	defer func() {
		if err := client.Deregister(); err != nil {
			log.Printf("Failed do deregister service: %v", err)
		}
	}()

	userRepo := repositories.NewUserRepository(db)
	userService := services.NewUserService(userRepo)

	r := router.NewRouter(userService)
	router := r.SetupRoutes()

	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	router.Run(":8086")
}
