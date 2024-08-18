package services

import (
	"github.com/Dialosoft/management-service/src/adapters/repositories"
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type forumServiceImpl struct {
	forumRepo repositories.ForumRepository
}

// CreateForum implements ForumService.
func (impl *forumServiceImpl) CreateForum(name string, description string, categoryID uuid.UUID) error {
	newForum := entities.Forum{
		Name:        name,
		Description: description,
	}

	err := impl.forumRepo.Create(&newForum, categoryID)
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

// UpdateForumCategoryOwner implements ForumService.
func (impl *forumServiceImpl) UpdateForumCategoryOwner(id uuid.UUID, categoryID uuid.UUID) error {
	err := impl.forumRepo.UpdateCategoryOwner(id, categoryID)
	if err != nil {
		return err
	}
	return nil
}

// UpdateForumName implements ForumService.
func (impl *forumServiceImpl) UpdateForumName(id uuid.UUID, name string) error {
	forum, err := impl.forumRepo.FindByID(id)
	if err != nil {
		return err
	}

	forum.Name = name

	err = impl.forumRepo.Update(forum)
	if err != nil {
		return err
	}

	return nil
}

// UpdateForumDescription implements ForumService.
func (impl *forumServiceImpl) UpdateForumDescription(id uuid.UUID, description string) error {
	forum, err := impl.forumRepo.FindByID(id)
	if err != nil {
		return err
	}

	forum.Description = description

	err = impl.forumRepo.Update(forum)
	if err != nil {
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
