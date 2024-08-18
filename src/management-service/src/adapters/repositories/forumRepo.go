package repositories

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type ForumRepository interface {
	FindAll() ([]*entities.Forum, error)
	FindAllWithDeleted() ([]*entities.Forum, error)
	FindByID(uuid uuid.UUID) (*entities.Forum, error)
	FindByIDWithDeleted(uuid uuid.UUID) (*entities.Forum, error)
	FindByName(name string) (*entities.Forum, error)
	Create(forum *entities.Forum, categoryOwner *entities.Category) error
	Update(forum *entities.Forum) error
	Delete(uuid uuid.UUID) error
	Restore(uuid uuid.UUID) error
}
