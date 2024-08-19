package repositories

import (
	"github.com/Dialosoft/user-service/src/domain/entities"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type userRepositoryImpl struct {
	db *gorm.DB
}

func NewUserRepository(db *gorm.DB) UserRepository {
	return &userRepositoryImpl{db: db}
}

// Find a user by the uuid
func (r *userRepositoryImpl) FindByID(uuid uuid.UUID) (*entities.User, error) {
	var user entities.User
	if err := r.db.Preload("Role").Where("id = ?", uuid.String()).First(&user).Error; err != nil {
		return nil, err
	}

	return &user, nil
}

// Find a user by the username
func (r *userRepositoryImpl) FindByUsername(username string) (*entities.User, error) {
	var user entities.User
	if err := r.db.Preload("Role").Where("username = ?", username).First(&user).Error; err != nil {
		return nil, err
	}

	return &user, nil
}

// Update a user from the repository
func (r *userRepositoryImpl) Update(user *entities.User) error {
	return r.db.Save(user).Error
}

// Delete a user from the repository
func (r *userRepositoryImpl) Delete(uuid uuid.UUID) error {
	return r.db.Delete(&entities.User{}, uuid).Error
}
