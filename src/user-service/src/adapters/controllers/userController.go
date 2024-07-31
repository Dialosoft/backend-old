package controllers

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func Home(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"message": "home!"})

}

func ChangeEmailController(c *gin.Context) {

}

func ChangeUserAvatarController(c *gin.Context) {

}

func ChangePasswordController(c *gin.Context) {

}
