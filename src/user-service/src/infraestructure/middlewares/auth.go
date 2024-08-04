package middlewares

import (
	"fmt"
	"net/http"

	"github.com/biznetbb/user-service/src/infraestructure/registry"
	"github.com/gin-gonic/gin"
)

func AuthServiceMiddleware(c *gin.Context) {
	serviceURL, err := registry.GetServiceUrl("auth-service")
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "unable to find auth-service"})
		c.Abort()
		return
	}

	resp, err := http.Get(serviceURL + "/bizznet-bb/validate")
	if err != nil || resp.StatusCode != http.StatusOK {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "error calling auth-service"})
		c.Abort()
		fmt.Println(err)
		return
	}

	c.Next()
}
