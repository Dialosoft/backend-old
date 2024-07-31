package services

import (
	"errors"

	"github.com/google/uuid"
)

// implemtation of the user services

type UserService interface {
	ChangeEmail()
	ChangeAvatar()
	ChangePassword()
}

func ChangeEmail(userId uuid.UUID, newMail string) error {
	if newMail == "" {
		return errors.New("can't no be empty")
	}

	return nil
}

func ChangeAvatar(userId uuid.UUID, avatar []byte) error {

}

func ChangePassword() {

}
