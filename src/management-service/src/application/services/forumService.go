package services

import (
	"github.com/Dialosoft/post-manager-service/src/domain/entities"
	"github.com/google/uuid"
)

type ForumService interface {
	GetAllForums() ([]*entities.Forum, error)
	GetForumByID(id uuid.UUID) (*entities.Forum, error)
	GetForumByName(name string) (*entities.Forum, error)
	CreateForum(name string) error
	RenameForum(newName string) error
	DeleteForum(id uuid.UUID) error
}
