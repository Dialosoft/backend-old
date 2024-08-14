package services

import (
	"io"

	"github.com/Dialosoft/user-service/src/domain/entities"
	"github.com/google/uuid"
)

type UserService interface {
	GetUser(username string) (*entities.User, error)
	ChangeEmail(userID uuid.UUID, newMail string) error
	ChangeAvatar(userID uuid.UUID, avatar io.Reader) error
}
