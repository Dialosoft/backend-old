package services

import (
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
)

type CategoryService interface {
	GetAllCategories() ([]*entities.Category, error)
	GetCategoryByID(id uuid.UUID) (*entities.Category, error)
	CreateCategory(name string) error
	RenameCategory(newName string) error
	DeleteCategory(id uuid.UUID) error
}
