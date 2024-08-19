package main

import (
	"fmt"
	"log"
	"net"
	"time"

	docs "github.com/Dialosoft/management-service/docs"
	"github.com/Dialosoft/management-service/src/adapters/repositories"
	"github.com/Dialosoft/management-service/src/adapters/routers"
	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/Dialosoft/management-service/src/infraestructure/db"
	"github.com/Dialosoft/management-service/src/infraestructure/registry"
	"github.com/gin-gonic/gin"
	swaggerfiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"gorm.io/gorm"
)

func main() {
	var database *gorm.DB
	var err error

	EUREKAURL := "http://registry-service:8761/eureka" //os.Getenv("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE")
	appName := "management-microservice"
	hostname := "management-microservice"
	ipAddr := getoutBoundIp().String()
	port := 8087

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

	client := registry.NewEurekaClient(EUREKAURL, appName, hostname, ipAddr, port)

	for {
		if err := client.Register(); err != nil {
			log.Printf("Failed to register service: %v", err)
			time.Sleep(5 * time.Second)
		} else {
			break
		}
	}

	router := gin.Default()

	forumRepo := repositories.NewForumRepository(database)
	categoryRepo := repositories.NewCategoryRepository(database)

	forumService := services.NewForumService(forumRepo)
	categoryService := services.NewCategoryService(categoryRepo)

	forumRouter := routers.NewForumRouter(forumService)
	categoryRouter := routers.NewCategoryRouter(categoryService)

	management := router.Group("/management-service")

	{
		forumRouter.SetupForumRoutes(management)
		categoryRouter.SetupCategoryRoutes(management)
	}

	docs.SwaggerInfo.BasePath = "/swagger"
	docs := management.Group("/swagger")

	docs.GET("/*any", ginSwagger.WrapHandler(swaggerfiles.Handler))

	router.Run(fmt.Sprintf(":%d", port))
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
