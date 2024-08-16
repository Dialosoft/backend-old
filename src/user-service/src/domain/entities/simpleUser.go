package entities

import "github.com/google/uuid"

type SimpleUser struct {
	ID       uuid.UUID
	Username string
	Roles    []Role
}
