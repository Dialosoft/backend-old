package entities

import (
	"time"

	"github.com/google/uuid"
)

type User struct {
	ID        uuid.UUID  `gorm:"type:uuid;primary_key" json:"uuid"`
	Username  string     `gorm:"type:varchar(30);unique;not null" json:"username"`
	Email     string     `gorm:"type:varchar(100);unique;not null" json:"email"`
	Password  string     `gorm:"type:varchar(100);not null" json:"password"`
	Locked    bool       `gorm:"not null;default:false" json:"locked"`
	Disable   bool       `gorm:"not null;default:false" json:"disable"`
	RoleID    string     `gorm:"not null" json:"roleId"`
	Role      Role       `gorm:"foreignKey:RoleID;references:ID" json:"role"`
	CreatedAt time.Time  `gorm:"not null;autoCreateTime" json:"created_at"`
	UpdatedAt time.Time  `gorm:"not null;autoUpdateTime" json:"updated_at"`
	DeletedAt *time.Time `json:"deleted_at,omitempty"`
}
