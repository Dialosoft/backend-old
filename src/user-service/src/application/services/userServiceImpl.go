package services

import (
	"errors"
	"fmt"
	"image"
	"image/jpeg"
	_ "image/png"
	"io"
	"os"
	"path/filepath"

	"github.com/Dialosoft/user-service/src/adapters/repositories"
	"github.com/Dialosoft/user-service/src/domain/entities"
	"github.com/google/uuid"
)

type userServiceImpl struct {
	userRepo repositories.UserRepository
}

func NewUserService(userRepo repositories.UserRepository) UserService {
	return &userServiceImpl{userRepo: userRepo}
}

func (s *userServiceImpl) GetUser(username string) (*entities.User, error) {
	if username == "" {
		return nil, errors.New("any of the parameters cannot be empty")
	}

	user, err := s.userRepo.FindByUsername(username)
	if err != nil {
		return nil, err
	}

	return user, nil
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

func (s *userServiceImpl) ChangeAvatar(userID uuid.UUID, avatar io.Reader) error {

	filePath := filepath.Join("avatars", fmt.Sprintf("%s.jpg", userID))
	err := saveCompressedAvatarToFile(filePath, avatar)
	if err != nil {
		return err
	}

	return nil
}

func saveCompressedAvatarToFile(filePath string, avatar io.Reader) error {
	img, _, err := image.Decode(avatar)
	if err != nil {
		fmt.Println(err)
		return err
	}

	outFile, err := os.Create(filePath)
	if err != nil {
		return err
	}
	defer outFile.Close()

	var opt jpeg.Options
	opt.Quality = 50
	err = jpeg.Encode(outFile, img, &opt)
	if err != nil {
		return err
	}

	return nil
}
