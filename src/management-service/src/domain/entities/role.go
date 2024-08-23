package entities

type Role struct {
	ID         string `gorm:"primary_key" json:"id"`
	AdminRole  bool   `gorm:"default:false" json:"admin_role"`
	ModRole    bool   `gorm:"default:false" json:"mod_role"`
	Permission int    `json:"permission"`
	RoleType   string `gorm:"type:varchar(50)" json:"role_type"`
}
