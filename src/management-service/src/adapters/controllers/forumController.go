package controllers

import (
	"github.com/Dialosoft/management-service/src/application/services"
	"github.com/gin-gonic/gin"
)

func GetAllForums(c *gin.Context, service services.ForumService) {
	forums, err := service.GetAllForums()
	if err != nil {
		panic("")
	}

	_ = forums
}
