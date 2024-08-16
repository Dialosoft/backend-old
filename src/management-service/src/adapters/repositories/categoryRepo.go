package repositories

import (
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
)

type CategoryRepository interface {
	FindAll() []*entities.Category
	FindByID(uuid uuid.UUID) (*entities.Category, error)
	FindByName(name string) (*entities.Category, error)
	Update(category *entities.Category) error
	Delete(uuid uuid.UUID) error
}
