package registry

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/hudl/fargo"
)

type EurekaClient struct {
	Conn     *fargo.EurekaConnection
	Instance *fargo.Instance
}

func NewEurekaClient(eurekaURL, appname, hostname, ipAddr string, port int) *EurekaClient {
	conn := fargo.NewConn(eurekaURL)
	instanceID := strings.ToLower(appname) + ":" + strconv.Itoa(port)
	instance := &fargo.Instance{
		InstanceId:       instanceID,
		HostName:         hostname,
		App:              strings.ToLower(appname),
		IPAddr:           ipAddr,
		VipAddress:       strings.ToLower(appname),
		SecureVipAddress: strings.ToLower(appname),
		Status:           fargo.UP,
		Port:             port,
		DataCenterInfo:   fargo.DataCenterInfo{Name: "MyOwn"},
		LeaseInfo: fargo.LeaseInfo{
			DurationInSecs:        90,
			RenewalIntervalInSecs: 30,
		},
	}
	log.Println(instanceID)

	return &EurekaClient{Conn: &conn, Instance: instance}
}

func (client *EurekaClient) Register() error {
	err := client.Conn.RegisterInstance(client.Instance)
	if err != nil {
		log.Printf("Error registering service with eureka: %v", err)
		return err
	}
	log.Println("Service registered successfully with eureka")

	go client.startHeartbeat()

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

func (client *EurekaClient) startHeartbeat() {
	ticker := time.NewTicker(time.Second * 30)
	defer ticker.Stop()

	for range ticker.C {
		client.renewInstance()
	}
}

func (client *EurekaClient) renewInstance() {
	url := fmt.Sprintf("%s/apps/%s/%s", client.Conn.ServiceUrls[0], client.Instance.App, client.Instance.InstanceId)
	req, err := http.NewRequest(http.MethodPut, url, nil)
	if err != nil {
		log.Printf("Error creating renewal request: %v", err)
		return
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		log.Printf("Error sending renewal request: %v", err)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		bodyBytes, _ := io.ReadAll(resp.Body)
		bodyString := string(bodyBytes)
		log.Printf("Failed to renew instance: status code %d", resp.StatusCode)
		log.Printf("response body: %s", bodyString)
		log.Print("url: " + url)
	} else {
		log.Println("Instance renewed with eureka")
	}
}

func (client *EurekaClient) GetServiceURL(appname string) (string, error) {
	app, err := client.Conn.GetApp(appname)
	if err != nil {
		return "", err
	}

	if len(app.Instances) == 0 {
		return "", fmt.Errorf("no instances found for app %s", appname)
	}

	instance := app.Instances[0]
	return fmt.Sprintf("http://%s:%d", instance.IPAddr, instance.Port), nil
}
