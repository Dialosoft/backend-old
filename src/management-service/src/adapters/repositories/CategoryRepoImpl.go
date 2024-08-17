package repositories

import (
	"fmt"

	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type categoryRepositoryImpl struct {
	db *gorm.DB
}

// Create implements CategoryRepository.
func (repo *categoryRepositoryImpl) Create(category *entities.Category) error {
	result := repo.db.Create(category)
	if result.Error != nil {
		return result.Error
	}
	return nil
}

// FindAll implements CategoryRepository.
func (repo *categoryRepositoryImpl) FindAll() ([]*entities.Category, error) {
	var Categories []*entities.Category
	result := repo.db.Find(&Categories)

	if result.Error != nil {
		return nil, result.Error
	}
	return Categories, nil
}

// FindByID implements CategoryRepository.
func (repo *categoryRepositoryImpl) FindByID(uuid uuid.UUID) (*entities.Category, error) {
	var category entities.Category
	result := repo.db.First(&category, "id = ?", uuid.String())
	if result.Error != nil {
		return nil, result.Error
	}

	return &category, nil
}

// FindByName implements CategoryRepository.
func (repo *categoryRepositoryImpl) FindByName(name string) (*entities.Category, error) {
	var category entities.Category
	result := repo.db.First(&category, "name = ?", name)
	if result.Error != nil {
		return nil, result.Error
	}

	return &category, nil
}

// FindAllIncludingDeleted implements CategoryRepository.
func (repo *categoryRepositoryImpl) FindAllIncludingDeleted() ([]*entities.Category, error) {
	var categories []*entities.Category

	result := repo.db.Unscoped().Find(&categories)

	if result.Error != nil {
		return nil, result.Error
	}

	return categories, nil
}

// Update implements CategoryRepository.
func (repo *categoryRepositoryImpl) Update(category *entities.Category) error {
	result := repo.db.Model(&category).Where("id = ?", category.ID).Updates(category)

	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Restore implements ForumRepository (Restaurar Soft Delete).
func (repo *categoryRepositoryImpl) Restore(id uuid.UUID) error {

	result := repo.db.Unscoped().Model(&entities.Category{}).Where("id = ?", id).Update("deleted_at", nil)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Delete implements CategoryRepository.
func (repo *categoryRepositoryImpl) Delete(uuid uuid.UUID) error {

	result := repo.db.Delete(&entities.Category{}, uuid)

	if result.Error != nil {
		return result.Error
	}

	if result.RowsAffected == 0 {
		return fmt.Errorf("no category found with ID %v", uuid)
	}

	return nil
}

func NewCategoryRepository(db *gorm.DB) CategoryRepository {
	return &categoryRepositoryImpl{db: db}
}
