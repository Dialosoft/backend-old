package services

import (
	"errors"

	"github.com/biznetbb/user-service/src/adapters/repositories"
	"github.com/google/uuid"
)

type userServiceImpl struct {
	userRepo repositories.UserRepository
}

func NewUserService(userRepo repositories.UserRepository) UserService {
	return &userServiceImpl{userRepo: userRepo}
}

func (s *userServiceImpl) ChangeEmail(userID uuid.UUID, newMail string) error {
	if userID == uuid.Nil || newMail == "" {
		return errors.New("any of the parameters cannot be empty")
	}

	user, err := s.userRepo.FindByID(userID)
	if err != nil {
		return err
	}

	user.Email = newMail

	return s.userRepo.Update(user)
}

func (s *userServiceImpl) ChangeAvatar(userID uuid.UUID, avatar []byte) error {
	if len(avatar) == 0 {
		return errors.New("avatar cannot be empty")
	}

	user, err := s.userRepo.FindByID(userID)
	if err != nil {
		return err
	}

	user.Avatar = avatar
	return s.userRepo.Update(user)
}
