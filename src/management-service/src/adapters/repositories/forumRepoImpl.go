package repositories

import (
	"errors"

	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type forumRepositoryImpl struct {
	db *gorm.DB
}

// Create implements ForumRepository.
func (repo *forumRepositoryImpl) Create(forum *entities.Forum, categoryID uuid.UUID) error {
	var category entities.Category
	result := repo.db.Find(&category, "id = ?", categoryID.String())
	if result.Error != nil {
		return result.Error
	}

	forum.CategoryID = category.ID.String()
	forum.Category = *&category

	result = repo.db.Create(forum)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Delete implements ForumRepository (Soft Delete).
func (repo *forumRepositoryImpl) Delete(id uuid.UUID) error {
	result := repo.db.Delete(&entities.Forum{}, id)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Restore implements ForumRepository (Restaurar Soft Delete).
func (repo *forumRepositoryImpl) Restore(id uuid.UUID) error {

	result := repo.db.Unscoped().Model(&entities.Forum{}).Where("id = ?", id).Update("deleted_at", nil)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// FindAll implements ForumRepository (sin foros eliminados).
func (repo *forumRepositoryImpl) FindAll() ([]*entities.Forum, error) {
	var forums []*entities.Forum
	result := repo.db.Find(&forums)
	if result.Error != nil {
		return nil, result.Error
	}

	return forums, nil
}

// FindAllWithDeleted implements ForumRepository (incluye foros eliminados).
func (repo *forumRepositoryImpl) FindAllWithDeleted() ([]*entities.Forum, error) {
	var forums []*entities.Forum
	result := repo.db.Unscoped().Find(&forums)
	if result.Error != nil {
		return nil, result.Error
	}

	return forums, nil
}

// FindByID implements ForumRepository (sin foros eliminados).
func (repo *forumRepositoryImpl) FindByID(id uuid.UUID) (*entities.Forum, error) {
	var forum entities.Forum
	result := repo.db.First(&forum, "id = ?", id)
	if errors.Is(result.Error, gorm.ErrRecordNotFound) {
		return nil, nil
	}
	if result.Error != nil {
		return nil, result.Error
	}

	return &forum, nil
}

// FindByIDWithDeleted implements ForumRepository (incluye foros eliminados).
func (repo *forumRepositoryImpl) FindByIDWithDeleted(id uuid.UUID) (*entities.Forum, error) {
	var forum entities.Forum
	result := repo.db.Unscoped().First(&forum, "id = ?", id)
	if errors.Is(result.Error, gorm.ErrRecordNotFound) {
		return nil, nil
	}
	if result.Error != nil {
		return nil, result.Error
	}

	return &forum, nil
}

// FindByName implements ForumRepository.
func (repo *forumRepositoryImpl) FindByName(name string) (*entities.Forum, error) {
	var forum entities.Forum
	result := repo.db.First(&forum, "name = ?", name)
	if errors.Is(result.Error, gorm.ErrRecordNotFound) {
		return nil, nil
	}
	if result.Error != nil {
		return nil, result.Error
	}

	return &forum, nil
}

// Update implements ForumRepository.
func (repo *forumRepositoryImpl) Update(forum *entities.Forum) error {

	result := repo.db.Model(forum).Updates(forum)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

func NewForumRepository(db *gorm.DB) ForumRepository {
	return &forumRepositoryImpl{db: db}
}
