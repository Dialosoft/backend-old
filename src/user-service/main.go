package main

import (
	"fmt"
	"log"
	"net"
	"os"
	"time"

	"github.com/Dialosoft/user-service/src/adapters/repositories"
	"github.com/Dialosoft/user-service/src/adapters/router"
	"github.com/Dialosoft/user-service/src/application/services"
	"github.com/Dialosoft/user-service/src/infraestructure/db"
	"github.com/Dialosoft/user-service/src/infraestructure/registry"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func main() {

	var database *gorm.DB
	var err error

	EUREKAURL := "http://registry-service:8761/eureka" //os.Getenv("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE")
	appName := "user-microservice"
	hostname := "user-microservice"
	ipAddr := getoutBoundIp().String()
	port := 8086

	for {
		database, err = db.DBConnection()
		if err != nil {
			log.Printf("error initializing database: %v", err)
			time.Sleep(3 * time.Second)
		} else {
			break
		}
	}

	fmt.Println("Database connection successful", database)

	err = os.MkdirAll("avatars", os.ModePerm)
	if err != nil {
		fmt.Printf("error creating avatars directory: %v\n", err)
	}

	client := registry.NewEurekaClient(EUREKAURL, appName, hostname, ipAddr, port)

	for {
		if err := client.Register(); err != nil {
			log.Printf("Failed to register service: %v", err)
			time.Sleep(5 * time.Second)
		} else {
			break
		}
	}

	userRepo := repositories.NewUserRepository(database)
	userService := services.NewUserService(userRepo)

	r := router.NewRouter(userService)
	router := r.SetupRoutes()

	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	router.Run(":8086")
}

func getoutBoundIp() net.IP {
	conn, err := net.Dial("udp", "8.8.8.8:80")
	if err != nil {
		log.Fatal(err)
	}

	defer conn.Close()
	localAddr := conn.LocalAddr().(*net.UDPAddr)
	return localAddr.IP
}
