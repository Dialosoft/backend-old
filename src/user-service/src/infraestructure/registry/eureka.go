package registry

import (
	"fmt"
	"log"

	"github.com/hudl/fargo"
)

type EurekaClient struct {
	Conn     *fargo.EurekaConnection
	Instance *fargo.Instance
}

func NewEurekaClient(eurekaURL, appname, hostname, ipAddr string, port int) *EurekaClient {
	conn := fargo.NewConn(eurekaURL)
	instance := &fargo.Instance{
		HostName:         hostname,
		App:              appname,
		IPAddr:           ipAddr,
		VipAddress:       appname,
		SecureVipAddress: appname,
		Port:             port,
		DataCenterInfo:   fargo.DataCenterInfo{Name: "MyOwn"},
	}

	return &EurekaClient{Conn: &conn, Instance: instance}
}

func (client *EurekaClient) Register() error {
	err := client.Conn.RegisterInstance(client.Instance)
	if err != nil {
		log.Printf("Error registering Service with eureka: %v", err)
		return err
	}
	log.Println("Service registered successfully with eureka")
	return nil
}

func (client *EurekaClient) Deregister() error {
	err := client.Conn.DeregisterInstance(client.Instance)
	if err != nil {
		log.Printf("Error deregistering service from eureka: %v", err)
		return err
	}

	log.Println("Service deregistered successfully from eureka")
	return nil
}

func (client *EurekaClient) GetServiceURL(appname string) (string, error) {
	app, err := client.Conn.GetApp(appname)
	if err != nil {
		return "", err
	}

	if len(app.Instances) == 0 {
		return "", fmt.Errorf("no instances found for app %s", err)
	}

	instance := app.Instances[0]
	return fmt.Sprintf("http://%s:%d", instance.IPAddr, instance.Port), nil
}
