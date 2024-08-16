package repositories

import (
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type categoryRepositoryImpl struct {
	db *gorm.DB
}

// Delete implements CategoryRepository.
func (*categoryRepositoryImpl) Delete(uuid uuid.UUID) error {
	panic("unimplemented")
}

// FindAll implements CategoryRepository.
func (*categoryRepositoryImpl) FindAll() []*entities.Category {
	panic("unimplemented")
}

// FindByID implements CategoryRepository.
func (*categoryRepositoryImpl) FindByID(uuid uuid.UUID) (*entities.Category, error) {
	panic("unimplemented")
}

// FindByName implements CategoryRepository.
func (*categoryRepositoryImpl) FindByName(name string) (*entities.Category, error) {
	panic("unimplemented")
}

// Update implements CategoryRepository.
func (*categoryRepositoryImpl) Update(category *entities.Category) error {
	panic("unimplemented")
}

func NewCategoryRepository(db *gorm.DB) CategoryRepository {
	return &categoryRepositoryImpl{db: db}
}
