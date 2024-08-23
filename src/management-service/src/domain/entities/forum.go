package entities

import (
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Forum struct {
	ID          uuid.UUID      `gorm:"type:uuid;primary_key" json:"id"`
	Name        string         `gorm:"type:varchar(100);unique;not null" json:"name"`
	Description string         `gorm:"type:varchar(255)" json:"description"`
	IsActive    bool           `gorm:"default:true" json:"is_active"`
	Type        string         `gorm:"type:varchar(255);not null" json:"type"`
	PostCount   uint32         `gorm:"default:0" json:"post_count"`
	CategoryID  string         `gorm:"not null" json:"category_id"`
	Category    Category       `gorm:"foreignKey:CategoryID;references:ID" json:"category"`
	CreatedAt   time.Time      `gorm:"autoCreateTime" json:"created_at"`
	UpdatedAt   time.Time      `gorm:"autoUpdateTime" json:"updated_at"`
	DeleteAt    gorm.DeletedAt `gorm:"index" json:"deleted_at,omitempty"`
}

func (f *Forum) BeforeCreate(tx *gorm.DB) (err error) {
	if f.ID == uuid.Nil {
		f.ID = uuid.New()
	}
	return nil
}
