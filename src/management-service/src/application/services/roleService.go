package services

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type RoleService interface {
	GetRole(id uuid.UUID) (*entities.Role, error)
	GetAllRoles() error
	CreateRole(role entities.Role) error
	UpdateRole(id uuid.UUID) error
	DeleteRole(id uuid.UUID) error
	RestoreRole(id uuid.UUID) error
}
