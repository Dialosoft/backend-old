package repositories

import (
	"github.com/biznetbb/user-service/src/domain/entities"
	"gorm.io/gorm"
)

type userRepository struct {
	db *gorm.DB
}

func NewUserRepository(db *gorm.DB) UserRepository {
	return &userRepository{db: db}
}

// Create a new user in the database
// func (r *userRepository) Create(user *entities.User) error {
// 	return r.db.Create(user).Error
// }

// Find a user by the uuid
func (r *userRepository) FindByID(uuid string) (*entities.User, error) {
	var user entities.User
	if err := r.db.First(&user, uuid).Error; err != nil {
		return nil, err
	}

	return &user, nil
}

// Find a user by the username
func (r *userRepository) FindByUsername(username string) (*entities.User, error) {
	var user entities.User
	if err := r.db.Where("username = ?", username).First(&user).Error; err != nil {
		return nil, err
	}

	return &user, nil
}

// Update a user from the repository
func (r *userRepository) Update(user *entities.User) error {
	return r.db.Save(user).Error
}

// Delete a user from the repository
func (r *userRepository) Delete(uuid string) error {
	return r.db.Delete(&entities.User{}, uuid).Error
}
