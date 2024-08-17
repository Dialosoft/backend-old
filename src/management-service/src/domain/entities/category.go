package entities

import "github.com/google/uuid"

type Category struct {
	ID   uuid.UUID `gorm:"type:uuid;primary_key" json:"uuid"`
	Name string    `gorm:"type:varchar(30);unique;not null" json:"name"`
}
