package db

import (
	"fmt"
	"log"
	"os"
	"sync"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

var dbInstance *gorm.DB
var dbOnce sync.Once

func DBConnection() (*gorm.DB, error) {
	var err error

	dbOnce.Do(func() {
		DATASOURCE_HOST := os.Getenv("DATASOURCE_HOST")
		DATASOURCE_PORT := os.Getenv("DATASOURCE_PORT")
		DATASOURCE_DB := os.Getenv("DATASOURCE_DB")
		DATASOURCE_USERNAME := os.Getenv("DATASOURCE_USERNAME")
		DATASOURCE_PASSWORD := os.Getenv("DATASOURCE_PASSWORD")

		dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable", DATASOURCE_HOST, DATASOURCE_USERNAME, DATASOURCE_PASSWORD, DATASOURCE_DB, DATASOURCE_PORT)

		dbInstance, err = gorm.Open(postgres.Open(dsn), &gorm.Config{
			Logger: logger.Default.LogMode(logger.Info),
		})

		if err != nil {
			log.Printf("could not connect to database: %v", err)
			return
		}

		sqlDB, err := dbInstance.DB()
		if err != nil {
			log.Printf("colud not get the db object from sql: %v", err)
			return
		}

		sqlDB.SetMaxIdleConns(10)
		sqlDB.SetMaxOpenConns(100)
		// sqlDB.SetConnMaxLifetime(time.Hour)
	})

	return dbInstance, err
}
