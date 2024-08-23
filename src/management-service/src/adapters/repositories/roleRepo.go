package repositories

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type RoleRepository interface {
	FindAll() ([]*entities.Role, error)
	FindByID(uuid uuid.UUID) (*entities.Role, error)
	FindByType(roleType string) (*entities.Role, error)
	Create(role *entities.Role) error
	Update(id uuid.UUID, role *entities.Role) error
	Delete(id uuid.UUID) error
	Restore(id uuid.UUID) error
}
