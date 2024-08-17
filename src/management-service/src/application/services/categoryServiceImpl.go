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
func (impl *categoryServiceImpl) CreateCategory(name string, description string) error {
	newCategory := entities.Category{}
	newCategory.Name = name
	newCategory.Description = description

	err := impl.categoryRepo.Create(&newCategory)
	if err != nil {
		return err
	}

	return nil
}

// GetAllCategories implements CategoryService.
func (impl *categoryServiceImpl) GetAllCategories() ([]*entities.Category, error) {
	categories, err := impl.categoryRepo.FindAll()
	if err != nil {
		return nil, err
	}
	return categories, err
}

// GetCategoryByID implements CategoryService.
func (impl *categoryServiceImpl) GetCategoryByID(id uuid.UUID) (*entities.Category, error) {
	category, err := impl.categoryRepo.FindByID(id)
	if err != nil {
		return nil, err
	}
	return category, nil
}

// UpdateCategory implements CategoryService.
func (impl *categoryServiceImpl) UpdateCategory(name string, description string) error {
	category, err := impl.categoryRepo.FindByName(name)
	if err != nil {
		return err
	}

	if name != "" {
		category.Name = name
	}

	if description != "" {
		category.Description = description
	}

	err = impl.categoryRepo.Update(category)
	if err != nil {
		return err
	}

	return nil
}

// DeleteCategory implements CategoryService.
func (impl *categoryServiceImpl) DeleteCategory(id uuid.UUID) error {
	err := impl.categoryRepo.Delete(id)
	if err != nil {
		return err
	}

	return nil
}

// RestoreCategory implements CategoryService.
func (impl *categoryServiceImpl) RestoreCategory(id uuid.UUID) error {
	err := impl.categoryRepo.Restore(id)
	if err != nil {
		return err
	}

	return nil
}

func NewCategoryService(categoryRepo repositories.CategoryRepository) CategoryService {
	return &categoryServiceImpl{categoryRepo: categoryRepo}
}
