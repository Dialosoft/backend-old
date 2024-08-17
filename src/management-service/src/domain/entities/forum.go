package entities

import "github.com/google/uuid"

type Forum struct {
	ID         uuid.UUID `gorm:"type:uuid;primary_key" json:"uuid"`
	Name       string    `gorm:"type:varchar(30);unique;not null" json:"name"`
	CategoryID string    `gorm:"not null" json:"categoryId"`
	Category   Category  `gorm:"foreignKey:CategoryID;references:ID" json:"Category"`
}
