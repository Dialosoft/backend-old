package entities

import "time"

type User struct {
	UUID      string    `json:"uuid"`
	Username  string    `json:"username"`
	Email     string    `json:"email"`
	Avatar    []byte    `json:"avatar"`
	RoleId    Roles     `json:"roleId"`
	Locked    bool      `json:"locked"`
	Disable   bool      `json:"disable"`
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
	DeletedAt time.Time `json:"deleted_at"`
}
