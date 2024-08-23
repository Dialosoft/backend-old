package services

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type RoleService interface {
	GetAllRoles() ([]*entities.Role, error)
	GetRoleByID(id uuid.UUID) (*entities.Role, error)
	GetRoleByType(roleType string) (*entities.Role, error)
	CreateRole(roleType string, permission int, adminRole bool, modRole bool) error
	ChangeUserRole(roleID uuid.UUID, userID uuid.UUID) error
	UpdateRole(id uuid.UUID) error
	DeleteRole(id uuid.UUID) error
	RestoreRole(id uuid.UUID) error
}
