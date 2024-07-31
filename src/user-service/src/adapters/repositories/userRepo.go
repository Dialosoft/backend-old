package repositories

import (
	"github.com/biznetbb/user-service/src/domain/entities"
)

type UserRepository interface {
	FindByID(uuid string) (*entities.User, error)
	FindByUsername(username string) (*entities.User, error)
	Update(user *entities.User) error
	Delete(uuid string) error
}
