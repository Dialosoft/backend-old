package repositories

import (
	"github.com/biznetbb/user-service/src/domain/entities"
	"github.com/google/uuid"
)

type UserRepository interface {
	FindByID(uuid uuid.UUID) (*entities.User, error)
	FindByUsername(username string) (*entities.User, error)
	Update(user *entities.User) error
	Delete(uuid uuid.UUID) error
}
