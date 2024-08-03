package db

import (
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
		dsn := os.Getenv("DB_DSN")
		if dsn == "" {
			dsn = "host=localhost user=biznet password=biznet dbname=biznet-test port=5432 sslmode=disable"
		}

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
