package services

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type CategoryService interface {
	GetAllCategories() ([]*entities.Category, error)
	GetCategoryByID(id uuid.UUID) (*entities.Category, error)
	CreateCategory(name, description string) error
	UpdateCategory(id uuid.UUID, name, description string) error
	DeleteCategory(id uuid.UUID) error
	RestoreCategory(id uuid.UUID) error
}
