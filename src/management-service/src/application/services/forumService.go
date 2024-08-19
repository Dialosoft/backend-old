package services

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type ForumService interface {
	GetAllForums() ([]*entities.Forum, error)
	GetForumByID(id uuid.UUID) (*entities.Forum, error)
	GetForumByName(name string) (*entities.Forum, error)
	CreateForum(name, description string, categoryID uuid.UUID) error
	UpdateForumName(id uuid.UUID, name string) error
	UpdateForumDescription(id uuid.UUID, description string) error
	UpdateForumCategoryOwner(id uuid.UUID, categoryID uuid.UUID) error
	UpdateCountPostForum(id uuid.UUID, posts int) error
	DeleteForum(id uuid.UUID) error
	RestoreForum(id uuid.UUID) error
}
