package services

import (
	"github.com/Dialosoft/post-manager-service/src/adapters/repositories"
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
)

type forumServiceImpl struct {
	forumRepo repositories.ForumRepository
}

// CreateForum implements ForumService.
func (*forumServiceImpl) CreateForum(name string) error {
	panic("unimplemented")
}

// DeleteForum implements ForumService.
func (*forumServiceImpl) DeleteForum(id uuid.UUID) error {
	panic("unimplemented")
}

// GetAllForums implements ForumService.
func (*forumServiceImpl) GetAllForums() ([]*entities.Forum, error) {
	panic("unimplemented")
}

// GetForumByID implements ForumService.
func (*forumServiceImpl) GetForumByID(id uuid.UUID) (*entities.Forum, error) {
	panic("unimplemented")
}

// GetForumByName implements ForumService.
func (*forumServiceImpl) GetForumByName(name string) (*entities.Forum, error) {
	panic("unimplemented")
}

// RenameForum implements ForumService.
func (*forumServiceImpl) RenameForum(newName string) error {
	panic("unimplemented")
}

func NewForumService(forumRepo repositories.ForumRepository) ForumService {
	return &forumServiceImpl{forumRepo: forumRepo}
}
