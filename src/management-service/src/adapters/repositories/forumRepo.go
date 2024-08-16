package repositories

import (
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
)

type ForumRepository interface {
	FindAll() []*entities.Forum
	FindByID(uuid uuid.UUID) (*entities.Forum, error)
	FindByName(name string) (*entities.Forum, error)
	Update(forum *entities.Forum) error
	Delete(uuid uuid.UUID) error
}
