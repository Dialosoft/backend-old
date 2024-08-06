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

// Create a new user in the database
// func (r *userRepository) Create(user *entities.User) error {
// 	return r.db.Create(user).Error
// }

// Find a user by the uuid
func (r *userRepositoryImpl) FindByID(uuid uuid.UUID) (*entities.User, error) {
	var user entities.User
	if err := r.db.First(&user, uuid).Error; err != nil {
		return nil, err
	}

	return &user, nil
}

// Find a user by the username
func (r *userRepositoryImpl) FindByUsername(username string) (*entities.User, error) {
	var user entities.User
	if err := r.db.Where("username = ?", username).First(&user).Error; err != nil {
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
