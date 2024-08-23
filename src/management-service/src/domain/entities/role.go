package entities

import "github.com/google/uuid"

type Role struct {
	ID         uuid.UUID `gorm:"primary_key" json:"id"`
	AdminRole  bool      `gorm:"default:false" json:"admin_role"`
	ModRole    bool      `gorm:"default:false" json:"mod_role"`
	Permission int       `json:"permission"`
	RoleType   string    `gorm:"type:varchar(50)" json:"role_type"`
}
