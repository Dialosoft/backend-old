package entities

import "github.com/google/uuid"

type SimpleUser struct {
	ID       uuid.UUID `json:"id"`
	Username string    `json:"username"`
	Role     Role      `json:"role"`
}
