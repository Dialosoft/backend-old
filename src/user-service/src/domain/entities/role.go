package entities

import "github.com/google/uuid"

type RoleType string

const (
	Admin RoleType = "ADMIN"
	Mod   RoleType = "MOD"
	user  RoleType = "USER"
)

type Role struct {
	ID        uuid.UUID `gorm:"type:uuid;primary_key" json:"id"`
	RoleType  RoleType  `gorm:"type:varchar(50);not null;unique" json:"role_type"`
	AdminRole bool      `gorm:"not null" json:"admin_role"`
	ModRole   bool      `gorm:"not null" json:"mod_role"`
}
