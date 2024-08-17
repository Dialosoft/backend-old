package repositories

import (
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type forumRepositoryImpl struct {
	db *gorm.DB
}

// Create implements ForumRepository.
func (*forumRepositoryImpl) Create(name string, category *entities.Category) error {
	panic("unimplemented")
}

// Delete implements ForumRepository.
func (db *forumRepositoryImpl) Delete(uuid uuid.UUID) error {
	panic("unimplemented")
}

// FindAll implements ForumRepository.
func (db *forumRepositoryImpl) FindAll() []*entities.Forum {
	panic("unimplemented")
}

// FindByID implements ForumRepository.
func (db *forumRepositoryImpl) FindByID(uuid uuid.UUID) (*entities.Forum, error) {
	panic("unimplemented")
}

// FindByName implements ForumRepository.
func (db *forumRepositoryImpl) FindByName(name string) (*entities.Forum, error) {
	panic("unimplemented")
}

// Update implements ForumRepository.
func (db *forumRepositoryImpl) Update(forum *entities.Forum) error {
	panic("unimplemented")
}

func NewForumRepository(db *gorm.DB) ForumRepository {
	return &forumRepositoryImpl{db: db}
}
