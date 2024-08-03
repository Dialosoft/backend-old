package services

import (
	"bytes"
	"errors"
	"fmt"
	"image"
	"image/jpeg"
	"os"
	"path/filepath"

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

	filePath := filepath.Join("avatars", fmt.Sprintf("%s.png", userID))
	err := saveCompressedAvatarToFile(filePath, avatar)
	if err != nil {
		return err
	}

	return nil
}

func saveCompressedAvatarToFile(filePath string, avatar []byte) error {
	img, _, err := image.Decode(bytes.NewReader(avatar))
	if err != nil {
		return err
	}

	outFile, err := os.Create(filePath)
	if err != nil {
		return err
	}
	defer outFile.Close()

	var opt jpeg.Options
	opt.Quality = 80
	err = jpeg.Encode(outFile, img, &opt)
	if err != nil {
		return err
	}

	return nil
}
