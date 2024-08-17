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
func (impl *forumServiceImpl) CreateForum(name string, description string, categoryOwner *entities.Category) error {
	newForum := entities.Forum{
		Name:        name,
		Description: description,
	}

	err := impl.forumRepo.Create(&newForum, categoryOwner)
	if err != nil {
		return err
	}

	return nil
}

// GetAllForums implements ForumService.
func (impl *forumServiceImpl) GetAllForums() ([]*entities.Forum, error) {
	forums, err := impl.forumRepo.FindAll()
	if err != nil {
		return nil, err
	}

	return forums, nil
}

// GetForumByID implements ForumService.
func (impl *forumServiceImpl) GetForumByID(id uuid.UUID) (*entities.Forum, error) {
	forum, err := impl.forumRepo.FindByID(id)
	if err != nil {
		return nil, err
	}

	return forum, nil
}

// GetForumByName implements ForumService.
func (impl *forumServiceImpl) GetForumByName(name string) (*entities.Forum, error) {
	forum, err := impl.forumRepo.FindByName(name)
	if err != nil {
		return nil, err
	}

	return forum, nil
}

// RestoreForum implements ForumService.
func (impl *forumServiceImpl) RestoreForum(id uuid.UUID) error {
	forum, err := impl.forumRepo.FindByID(id)
	if err != nil {
		return err
	}

	if err = impl.forumRepo.Restore(forum.ID); err != nil {
		return err
	}

	return nil
}

// UpdateCountPostForum implements ForumService.
func (impl *forumServiceImpl) UpdateCountPostForum(id uuid.UUID, posts int) error {
	forum, err := impl.forumRepo.FindByID(id)
	if err != nil {
		return err
	}

	postsInUInt := uint32(posts)
	forum.PostCount = postsInUInt

	err = impl.forumRepo.Update(forum)
	if err != nil {
		return nil
	}

	return nil
}

// UpdateForum implements ForumService.
func (impl *forumServiceImpl) UpdateForum(id uuid.UUID, name string, description string, categoryOwner *entities.Category) error {
	forum, err := impl.forumRepo.FindByID(id)
	if err != nil {
		return err
	}

	if name != "" {
		forum.Name = name
	}

	if name != "" {
		forum.Description = description
	}

	if categoryOwner != nil {
		forum.CategoryID = categoryOwner.ID.String()
		forum.Category = *categoryOwner
	}

	err = impl.forumRepo.Update(forum)
	if err != nil {
		return err
	}

	return nil
}

// DeleteForum implements ForumService.
func (impl *forumServiceImpl) DeleteForum(id uuid.UUID) error {
	err := impl.forumRepo.Delete(id)
	if err != nil {
		return err
	}
	return nil
}

func NewForumService(forumRepo repositories.ForumRepository) ForumService {
	return &forumServiceImpl{forumRepo: forumRepo}
}
