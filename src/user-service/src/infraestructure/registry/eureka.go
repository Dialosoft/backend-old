package registry

import (
	"fmt"
	"log"
	"time"

	"github.com/hudl/fargo"
)

var eurekaConnection *fargo.EurekaConnection

func InitEurekaClient() {
	eurekaConnection = &fargo.EurekaConnection{
		ServiceUrls: []string{
			"http://registry-service:8761/eureka",
		},
		UseJson: true,
	}

	instance := fargo.Instance{
		HostName:       "localhost",
		Port:           8086,
		App:            "user-service",
		IPAddr:         "127.0.0.1",
		DataCenterInfo: fargo.DataCenterInfo{Name: fargo.MyOwn},
	}

	err := eurekaConnection.RegisterInstance(&instance)
	if err != nil {
		log.Fatalf("Error registering service with Eureka: %v", err)
	}

	go func() {
		for {
			eurekaConnection.HeartBeatInstance(&instance)
			time.Sleep(30 * time.Second)
		}
	}()
}

func GetServiceUrl(appName string) (string, error) {
	app, err := eurekaConnection.GetApp(appName)
	if err != nil {
		return "", err
	}

	if len(app.Instances) == 0 {
		return "", fmt.Errorf("no instances found for app: %s", appName)
	}

	instance := app.Instances[0]
	return fmt.Sprintf("http://%s:%d", instance.IPAddr, instance.Port), nil
}
