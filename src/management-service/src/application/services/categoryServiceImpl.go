package services

import (
	"github.com/Dialosoft/post-manager-service/src/adapters/repositories"
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
)

type categoryServiceImpl struct {
	categoryRepo repositories.CategoryRepository
}

// CreateCategory implements CategoryService.
func (*categoryServiceImpl) CreateCategory(name string) error {
	panic("unimplemented")
}

// DeleteCategory implements CategoryService.
func (*categoryServiceImpl) DeleteCategory(id uuid.UUID) error {
	panic("unimplemented")
}

// GetAllCategories implements CategoryService.
func (*categoryServiceImpl) GetAllCategories() ([]*entities.Category, error) {
	panic("unimplemented")
}

// GetCategoryByID implements CategoryService.
func (*categoryServiceImpl) GetCategoryByID(id uuid.UUID) (*entities.Category, error) {
	panic("unimplemented")
}

// RenameCategory implements CategoryService.
func (*categoryServiceImpl) RenameCategory(newName string) error {
	panic("unimplemented")
}

func NewCategoryService(categoryRepo repositories.CategoryRepository) CategoryService {
	return &categoryServiceImpl{categoryRepo: categoryRepo}
}
