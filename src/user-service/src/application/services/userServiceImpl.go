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
	"gorm.io/gorm"
)

type userServiceImpl struct {
	userRepo repositories.UserRepository
}

func NewUserService(userRepo repositories.UserRepository) UserService {
	return &userServiceImpl{userRepo: userRepo}
}

func (s *userServiceImpl) GetSimpleUser(username string) (*entities.SimpleUser, error) {
	simpleUser := &entities.SimpleUser{}

	if username == "" {
		return nil, errors.New("any of the parameters cannot be empty")
	}

	user, err := s.userRepo.FindByUsername(username)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, errors.New("user not found")
		} else {
			return nil, err
		}
	}

	simpleUser.ID = user.ID
	simpleUser.Username = user.Username
	simpleUser.Role = user.Role

	return simpleUser, nil
}

func (s *userServiceImpl) GetUser(username string) (*entities.User, error) {
	if username == "" {
		return nil, errors.New("any of the parameters cannot be empty")
	}

	user, err := s.userRepo.FindByUsername(username)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, errors.New("user not found")
		} else {
			return nil, err
		}
	}

	return user, nil
}

func (s *userServiceImpl) ChangeEmail(userID uuid.UUID, newMail string) error {
	if userID == uuid.Nil || newMail == "" {
		return errors.New("any of the parameters cannot be empty")
	}

	user, err := s.userRepo.FindByID(userID)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return errors.New("user not found")
		} else {
			return err
		}
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
